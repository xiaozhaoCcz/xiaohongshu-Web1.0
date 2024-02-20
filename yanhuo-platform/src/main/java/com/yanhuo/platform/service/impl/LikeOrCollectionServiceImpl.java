package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.im.ChatUtils;
import com.yanhuo.platform.service.*;
import com.yanhuo.xo.dao.LikeOrCollectionDao;
import com.yanhuo.xo.dto.LikeOrCollectionDTO;
import com.yanhuo.xo.entity.*;
import com.yanhuo.xo.vo.CommentVo;
import com.yanhuo.xo.vo.LikeOrCollectionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaozhao
 */
@Service
public class LikeOrCollectionServiceImpl extends ServiceImpl<LikeOrCollectionDao, LikeOrCollection> implements LikeOrCollectionService {
    @Autowired
    NoteService noteService;

    @Autowired
    AlbumNoteRelationService albumNoteRelationService;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentSyncService commentSyncService;

    @Autowired
    AlbumService albumService;

    @Autowired
    UserService userService;

    @Autowired
    ChatUtils chatUtils;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void likeOrCollectionByDTO(LikeOrCollectionDTO likeOrCollectionDTO) {
        String currentUid = AuthContextHolder.getUserId();
        // 点赞
        if (isLikeOrCollection(likeOrCollectionDTO)) {
            this.remove(new QueryWrapper<LikeOrCollection>().eq("uid", currentUid).eq("like_or_collection_id", likeOrCollectionDTO.getLikeOrCollectionId()).eq("type", likeOrCollectionDTO.getType()));
            updateLikeCollectionCount(likeOrCollectionDTO, -1);
        } else {
            // 点赞评论或者笔记
            LikeOrCollection likeOrCollection = ConvertUtils.sourceToTarget(likeOrCollectionDTO, LikeOrCollection.class);
            likeOrCollection.setTimestamp(System.currentTimeMillis());
            likeOrCollection.setUid(currentUid);
            this.save(likeOrCollection);
            updateLikeCollectionCount(likeOrCollectionDTO, 1);
            // 不是当前用户才进行通知
            if (!likeOrCollectionDTO.getPublishUid().equals(currentUid)) {
                chatUtils.sendMessage(likeOrCollectionDTO.getPublishUid(), 0);
            }
        }
    }

    private void updateLikeCollectionCount(LikeOrCollectionDTO likeOrCollectionDTO, int val) {
        switch (likeOrCollectionDTO.getType()) {
            case 1:
                Note likeNote = noteService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
                likeNote.setLikeCount(likeNote.getLikeCount() + val);
                noteService.updateById(likeNote);
                break;
            case 2:
                Comment comment = commentService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
                if (comment == null) {
                    CommentSync commentSync = commentSyncService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
                    commentSync.setLikeCount(commentSync.getLikeCount() + val);
                    commentSyncService.updateById(commentSync);
                } else {
                    comment.setLikeCount(comment.getLikeCount() + val);
                    commentService.updateById(comment);
                }
                break;
            case 3:
                String currentUid = AuthContextHolder.getUserId();
                Note collectionNote = noteService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
                //收藏图片
                collectionNote.setCollectionCount(collectionNote.getCollectionCount() + val);
                noteService.updateById(collectionNote);

                AlbumNoteRelation albumNoteRelation = new AlbumNoteRelation();
                albumNoteRelation.setNid(collectionNote.getId());
                if (val == 1) {
                    Album album = albumService.getOne(new QueryWrapper<Album>().eq("uid", currentUid).eq("type", 0));
                    Integer imgCount = collectionNote.getCount();
                    album.setImgCount(album.getImgCount() + imgCount);
                    if (StringUtils.isBlank(album.getAlbumCover())) {
                        album.setAlbumCover(collectionNote.getNoteCover());
                    }
                    albumNoteRelation.setAid(album.getId());
                    albumService.updateById(album);
                    albumNoteRelationService.save(albumNoteRelation);
                } else {
                    //
                    List<AlbumNoteRelation> albumNoteRelationList = albumNoteRelationService.list(new QueryWrapper<AlbumNoteRelation>().eq("nid", collectionNote.getId()));
                    Set<String> aids = albumNoteRelationList.stream().map(AlbumNoteRelation::getAid).collect(Collectors.toSet());
                    List<Album> albumList = albumService.listByIds(aids);
                    Album album = albumList.stream().filter(item -> item.getUid().equals(currentUid)).findFirst().orElse(null);
                    Integer imgCount = collectionNote.getCount();
                    long nums = album.getImgCount() - imgCount;
                    if (nums <= 0) {
                        album.setAlbumCover(null);
                    }
                    album.setImgCount(nums);
                    albumService.updateById(album);
                    albumNoteRelationService.remove(new QueryWrapper<AlbumNoteRelation>().eq("aid", album.getId()).eq("nid", collectionNote.getId()));
                }
                break;
            default:
                // 收藏专辑
                Album collectAlbum = albumService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
                collectAlbum.setCollectionCount(collectAlbum.getCollectionCount() + val);
                albumService.updateById(collectAlbum);
                break;
        }
    }

    @Override
    public boolean isLikeOrCollection(LikeOrCollectionDTO likeOrCollectionDTO) {
        String currentUid = AuthContextHolder.getUserId();
        long count = this.count(new QueryWrapper<LikeOrCollection>().eq("uid", currentUid).eq("like_or_collection_id", likeOrCollectionDTO.getLikeOrCollectionId()).eq("type", likeOrCollectionDTO.getType()));
        return count > 0;
    }

    @Override
    public Page<LikeOrCollectionVo> getNoticeLikeOrCollection(long currentPage, long pageSize) {
        Page<LikeOrCollectionVo> result = new Page<>();
        String currentUid = AuthContextHolder.getUserId();

        Page<LikeOrCollection> likeOrCollectionPage = this.page(new Page<>((int) currentPage, (int) pageSize), new QueryWrapper<LikeOrCollection>().eq("publish_uid", currentUid).ne("uid", currentUid).orderByDesc("create_date"));
        List<LikeOrCollection> likeOrCollectionList = likeOrCollectionPage.getRecords();
        long total = likeOrCollectionPage.getTotal();

        // TODO 可以使用多线程优化
        // 得到所有用户
        Set<String> uids = likeOrCollectionList.stream().map(LikeOrCollection::getUid).collect(Collectors.toSet());
        Map<String, User> userMap = new HashMap<>(16);
        if (!uids.isEmpty()) {
            userMap = userService.listByIds(uids).stream().collect(Collectors.toMap(User::getId, user -> user));
        }

        // notes
        Set<String> nids = likeOrCollectionList.stream().filter(e -> e.getType() == 1 || e.getType() == 3).map(LikeOrCollection::getLikeOrCollectionId).collect(Collectors.toSet());
        Map<String, Note> noteMap = new HashMap<>(16);
        if (!nids.isEmpty()) {
            noteMap = noteService.listByIds(nids).stream().collect(Collectors.toMap(Note::getId, note -> note));
        }

        // comments
        Set<String> cids = likeOrCollectionList.stream().filter(e -> e.getType() == 2).map(LikeOrCollection::getLikeOrCollectionId).collect(Collectors.toSet());
        Map<String, CommentVo> commentVoMap = new HashMap<>(16);
        if (!cids.isEmpty()) {
            List<Comment> commentList = commentService.listByIds(cids);
            Set<String> noteIds = commentList.stream().map(Comment::getNid).collect(Collectors.toSet());
            Map<String, Note> noteMap1 = noteService.listByIds(noteIds).stream().collect(Collectors.toMap(Note::getId, note -> note));

            commentList.forEach((item -> {
                CommentVo commentVo = ConvertUtils.sourceToTarget(item, CommentVo.class);
                Note note = noteMap1.get(item.getNid());
                commentVo.setNoteCover(note.getNoteCover());
                commentVoMap.put(item.getId(), commentVo);
            }));
        }

        //albums
        Set<String> aids = likeOrCollectionList.stream().filter(e -> e.getType() == 4).map(LikeOrCollection::getLikeOrCollectionId).collect(Collectors.toSet());
        Map<String, Album> albumMap = new HashMap<>(16);
        if (!aids.isEmpty()) {
            albumMap = albumService.listByIds(aids).stream().collect(Collectors.toMap(Album::getId, album -> album));
        }

        List<LikeOrCollectionVo> likeOrCollectionVoList = new ArrayList<>();


        for (LikeOrCollection model : likeOrCollectionList) {
            LikeOrCollectionVo likeOrCollectionVo = new LikeOrCollectionVo();
            User user = userMap.get(model.getUid());
            likeOrCollectionVo.setUid(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setTime(model.getTimestamp())
                    .setType(model.getType());

            switch (model.getType()) {
                case 2:
                    CommentVo commentVo = commentVoMap.get(model.getLikeOrCollectionId());
                    likeOrCollectionVo.setItemId(commentVo.getId())
                            .setItemCover(commentVo.getNoteCover())
                            .setContent(commentVo.getContent());
                    break;
                case 4:
                    Album album = albumMap.get(model.getLikeOrCollectionId());
                    likeOrCollectionVo.setItemId(album.getId())
                            .setItemCover(album.getAlbumCover())
                            .setContent(album.getTitle());
                    break;
                default:
                    Note note = noteMap.get(model.getLikeOrCollectionId());
                    likeOrCollectionVo.setItemId(note.getId())
                            .setItemCover(note.getNoteCover());
                    break;
            }

            likeOrCollectionVoList.add(likeOrCollectionVo);
        }
        result.setRecords(likeOrCollectionVoList);
        result.setTotal(total);
        return result;
    }
}
