package com.yanhuo.platform.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.result.ResultCodeEnum;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.client.EsClient;
import com.yanhuo.platform.client.OssClient;
import com.yanhuo.platform.service.*;
import com.yanhuo.xo.dao.NoteDao;
import com.yanhuo.xo.dto.NoteDTO;
import com.yanhuo.xo.entity.*;
import com.yanhuo.xo.vo.NoteSearchVo;
import com.yanhuo.xo.vo.NoteVo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaozhao
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteDao, Note> implements NoteService {
    @Autowired
    UserService userService;

    @Autowired
    TagNoteRelationService tagNoteRelationService;

    @Autowired
    TagService tagService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    EsClient esClient;

    @Autowired
    FollowerService followerService;

    @Autowired
    LikeOrCollectionService likeOrCollectionService;


    @Autowired
    CommentService commentService;

    @Autowired
    CommentSyncService commentSyncService;

    @Autowired
    AlbumNoteRelationService albumNoteRelationService;

    @Autowired
    OssClient ossClient;

    @Value("${oss.type}")
    Integer type;

    @NotNull
    private StringBuilder getTags(Note note, NoteDTO noteDTO) {
        List<String> tagList = noteDTO.getTagList();
        List<TagNoteRelation> tagNoteRelationList = new ArrayList<>();
        List<Tag> tagList1 = tagService.list();
        Map<String, Tag> tagMap = tagList1.stream().collect(Collectors.toMap(Tag::getTitle, tag -> tag));
        StringBuilder tags = new StringBuilder();
        if (!tagList.isEmpty()) {
            for (String tag : tagList) {
                TagNoteRelation tagNoteRelation = new TagNoteRelation();
                if (tagMap.containsKey(tag)) {
                    Tag tagModel = tagMap.get(tag);
                    tagNoteRelation.setTid(tagModel.getId());
                } else {
                    Tag model = new Tag();
                    model.setTitle(tag);
                    model.setLikeCount(1L);
                    tagService.save(model);
                    tagNoteRelation.setTid(model.getId());
                }
                tagNoteRelation.setNid(note.getId());
                tagNoteRelationList.add(tagNoteRelation);
                tags.append(tag);
            }
            tagNoteRelationService.saveBatch(tagNoteRelationList);
        }
        return tags;
    }


    @Override
    public NoteVo getNoteById(String noteId) {
        Note note = this.getById(noteId);
        if (note == null) {
            throw new YanHuoException(ResultCodeEnum.FAIL);
        }
        note.setViewCount(note.getViewCount() + 1);
        User user = userService.getById(note.getUid());
        NoteVo noteVo = ConvertUtils.sourceToTarget(note, NoteVo.class);
        noteVo.setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setTime(note.getUpdateDate().getTime());

        boolean follow = followerService.isFollow(user.getId());
        noteVo.setIsFollow(follow);

        String currentUid = AuthContextHolder.getUserId();
        List<LikeOrCollection> likeOrCollectionList = likeOrCollectionService.list(new QueryWrapper<LikeOrCollection>().eq("like_or_collection_id", noteId).eq("uid", currentUid));

        Set<Integer> types = likeOrCollectionList.stream().map(LikeOrCollection::getType).collect(Collectors.toSet());
        noteVo.setIsLike(types.contains(1));
        noteVo.setIsCollection(types.contains(3));


        //得到标签
        List<TagNoteRelation> tagNoteRelationList = tagNoteRelationService.list(new QueryWrapper<TagNoteRelation>().eq("nid", noteId));
        List<String> tids = tagNoteRelationList.stream().map(TagNoteRelation::getTid).collect(Collectors.toList());

        if (!tids.isEmpty()) {
            List<Tag> tagList = tagService.listByIds(tids);
            noteVo.setTagList(tagList);
        }

        this.updateById(note);
        return noteVo;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveNoteByDTO(String noteData, MultipartFile[] files) {
        String currentUid = AuthContextHolder.getUserId();
        NoteDTO noteDTO = JSONUtil.toBean(noteData, NoteDTO.class);
        Note note = ConvertUtils.sourceToTarget(noteDTO, Note.class);
        note.setUid(currentUid);
        boolean save = this.save(note);
        if (!save) {
            return null;
        }
        // TODO 存在数据一致性问题，需要往专辑中添加

        User user = userService.getById(currentUid);
        user.setTrendCount(user.getTrendCount() + 1);
        userService.updateById(user);
        // 获取所有的标签
        StringBuilder tags = getTags(note, noteDTO);

        Category category = categoryService.getById(note.getCid());
        Category parentCategory = categoryService.getById(note.getCpid());

        List<String> dataList = null;
        try {
            Result<List<String>> result = ossClient.saveBatch(files, type);
            dataList = result.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] urlArr = dataList.toArray(new String[dataList.size()]);
        String urls = JSONUtil.toJsonStr(urlArr);
        note.setUrls(urls);
        note.setNoteCover(urlArr[0]);
        this.updateById(note);

        // 往es中添加数据
        NoteSearchVo noteSearchVo = ConvertUtils.sourceToTarget(note, NoteSearchVo.class);
        noteSearchVo.setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setLikeCount(0L)
                .setCategoryName(category.getTitle())
                .setCategoryParentName(parentCategory.getTitle())
                .setTags(tags.toString())
                .setTime(note.getUpdateDate().getTime());
        esClient.addNote(noteSearchVo);
        return note.getId();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNoteByIds(List<String> noteIds) {
        List<Note> noteList = this.listByIds(noteIds);
        // TODO 这里需要优化，数据一致性问题
        noteList.forEach(item -> {
            String noteId = item.getId();
            esClient.deleteNote(noteId);

            String urls = item.getUrls();
            JSONArray objects = JSONUtil.parseArray(urls);
            Object[] array = objects.toArray();
            List<String> pathArr = new ArrayList<>();
            for (Object o : array) {
                pathArr.add((String) o);
            }
            ossClient.deleteBatch(pathArr, type);
            // TODO 可以使用多线程优化，
            // 删除点赞图片，评论，标签关系，收藏关系
            likeOrCollectionService.remove(new QueryWrapper<LikeOrCollection>().eq("like_or_collection_id", noteId));
            List<Comment> commentList = commentService.list(new QueryWrapper<Comment>().eq("nid", noteId));
            List<CommentSync> commentSyncList = commentSyncService.list(new QueryWrapper<CommentSync>().eq("nid", noteId));
            List<String> cids = commentList.stream().map(Comment::getId).collect(Collectors.toList());
            List<String> cids2 = commentSyncList.stream().map(CommentSync::getId).collect(Collectors.toList());
            if (!cids.isEmpty()) {
                likeOrCollectionService.remove(new QueryWrapper<LikeOrCollection>().in("like_or_collection_id", cids).eq("type", 2));
            }
            commentService.removeBatchByIds(cids);
            commentSyncService.removeBatchByIds(cids2);
            tagNoteRelationService.remove(new QueryWrapper<TagNoteRelation>().eq("nid", noteId));
            albumNoteRelationService.remove(new QueryWrapper<AlbumNoteRelation>().eq("nid", noteId));
        });
        this.removeBatchByIds(noteIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNoteByDTO(String noteData, MultipartFile[] files) {
        // TODO 需要解决数据一致性问题
        String currentUid = AuthContextHolder.getUserId();
        NoteDTO noteDTO = JSONUtil.toBean(noteData, NoteDTO.class);
        Note note = ConvertUtils.sourceToTarget(noteDTO, Note.class);
        note.setUid(currentUid);
        boolean flag = this.updateById(note);
        if (!flag) {
            return;
        }
        Category category = categoryService.getById(note.getCid());
        Category parentCategory = categoryService.getById(note.getCpid());
        List<String> dataList = null;
        try {
            Result<List<String>> result = ossClient.saveBatch(files, type);
            dataList = result.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 删除原来图片的地址
        String urls = note.getUrls();
        JSONArray objects = JSONUtil.parseArray(urls);
        Object[] array = objects.toArray();
        List<String> pathArr = new ArrayList<>();
        for (Object o : array) {
            pathArr.add((String) o);
        }
        ossClient.deleteBatch(pathArr, type);

        String[] urlArr = dataList.toArray(new String[dataList.size()]);
        String newUrls = JSONUtil.toJsonStr(urlArr);
        note.setUrls(newUrls);
        note.setNoteCover(urlArr[0]);
        this.updateById(note);

        // 删除原来的标签绑定关系
        tagNoteRelationService.remove(new QueryWrapper<TagNoteRelation>().eq("nid", note.getId()));
        // 重新绑定标签关系
        StringBuilder tags = getTags(note, noteDTO);

        User user = userService.getById(currentUid);

        NoteSearchVo noteSearchVo = ConvertUtils.sourceToTarget(note, NoteSearchVo.class);
        noteSearchVo.setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setCategoryName(category.getTitle())
                .setCategoryParentName(parentCategory.getTitle())
                .setTags(tags.toString())
                .setTime(note.getUpdateDate().getTime());
        esClient.updateNote(noteSearchVo);
    }

    @Override
    public Page<NoteVo> getHotPage(long currentPage, long pageSize) {
        return null;
    }

    @Override
    public boolean pinnedNote(String noteId) {
        String currentUid = AuthContextHolder.getUserId();
        Note note = this.getById(noteId);
        if (note.getPinned().equals(1)) {
            note.setPinned(0);
        } else {
            List<Note> noteList = this.list(new QueryWrapper<Note>().eq("uid", currentUid));
            long count = noteList.stream().filter(item -> item.getPinned() == 1).count();
            if (count >= 2) {
                throw new YanHuoException("最多只能置顶2个笔记");
            }
            note.setPinned(1);
        }
        return this.updateById(note);
    }
}
