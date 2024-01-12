package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.service.*;
import com.yanhuo.xo.dao.LikeOrCollectionDao;
import com.yanhuo.xo.dto.LikeOrCollectionDTO;
import com.yanhuo.xo.entity.*;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void likeOrCollectionByDTO(LikeOrCollectionDTO likeOrCollectionDTO) {
        String currentUid = AuthContextHolder.getUserId();
        // 点赞
        if(isLikeOrCollection(likeOrCollectionDTO)){
            this.remove(new QueryWrapper<LikeOrCollection>().eq("uid",currentUid).eq("like_or_collection_id",likeOrCollectionDTO.getLikeOrCollectionId()).eq("type",likeOrCollectionDTO.getType()));
            updateLikeCollectionCount(likeOrCollectionDTO,-1);
        }else{
            // 点赞评论或者笔记
            LikeOrCollection likeOrCollection = ConvertUtils.sourceToTarget(likeOrCollectionDTO, LikeOrCollection.class);
            likeOrCollection.setTimestamp(System.currentTimeMillis());
            likeOrCollection.setUid(currentUid);
            this.save(likeOrCollection);
            updateLikeCollectionCount(likeOrCollectionDTO,1);
            // TODO 发送消息
        }
    }

    private void updateLikeCollectionCount(LikeOrCollectionDTO likeOrCollectionDTO,int val) {
        switch (likeOrCollectionDTO.getType() ){
            case 1:
                Note likeNote = noteService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
                likeNote.setLikeCount(likeNote.getLikeCount()+val);
                noteService.updateById(likeNote);
                break;
            case 2:
                Comment comment = commentService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
                if(comment==null){
                    CommentSync commentSync = commentSyncService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
                    commentSync.setLikeCount(commentSync.getLikeCount()+val);
                    commentSyncService.updateById(commentSync);
                }else{
                    comment.setLikeCount(comment.getLikeCount()+val);
                    commentService.updateById(comment);
                }
                break;
            case 3:
                String currentUid = AuthContextHolder.getUserId();
                Note collectionNote = noteService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
                //收藏图片
                collectionNote.setCollectionCount(collectionNote.getCollectionCount()+val);
                noteService.updateById(collectionNote);

                AlbumNoteRelation albumNoteRelation = new AlbumNoteRelation();
                albumNoteRelation.setNid(collectionNote.getId());
                if(val==1){
                    Album album = albumService.getOne(new QueryWrapper<Album>().eq("uid", currentUid).eq("type", 0));
                    Integer imgCount = collectionNote.getCount();
                    album.setImgCount(album.getImgCount()+imgCount);

                    albumNoteRelation.setAid(album.getId());
                    albumService.updateById(album);
                    albumNoteRelationService.save(albumNoteRelation);
                }else{
                    //
                    List<AlbumNoteRelation> albumNoteRelationList = albumNoteRelationService.list(new QueryWrapper<AlbumNoteRelation>().eq("nid", collectionNote.getId()));
                    Set<String> aids = albumNoteRelationList.stream().map(AlbumNoteRelation::getAid).collect(Collectors.toSet());
                    List<Album> albumList = albumService.listByIds(aids);
                    Album album = albumList.stream().filter(item -> item.getUid().equals(currentUid)).findFirst().orElse(null);
                    Integer imgCount = collectionNote.getCount();
                    album.setImgCount(album.getImgCount()-imgCount);
                    albumService.updateById(album);
                    albumNoteRelationService.remove(new QueryWrapper<AlbumNoteRelation>().eq("aid",album.getId()).eq("nid",collectionNote.getId()));
                }
                break;
            default:
                // 收藏专辑
                Album collectAlbum = albumService.getById(likeOrCollectionDTO.getLikeOrCollectionId());
                collectAlbum.setCollectionCount(collectAlbum.getCollectionCount()+val);
                albumService.updateById(collectAlbum);
                break;
        }
    }

    @Override
    public boolean isLikeOrCollection(LikeOrCollectionDTO likeOrCollectionDTO) {
        String currentUid = AuthContextHolder.getUserId();
        long count = this.count(new QueryWrapper<LikeOrCollection>().eq("uid",currentUid).eq("like_or_collection_id",likeOrCollectionDTO.getLikeOrCollectionId()).eq("type",likeOrCollectionDTO.getType()));
        return count>0;
    }
}
