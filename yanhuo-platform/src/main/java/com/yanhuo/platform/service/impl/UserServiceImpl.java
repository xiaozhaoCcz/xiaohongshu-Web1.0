package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.service.LikeOrCollectionService;
import com.yanhuo.platform.service.NoteService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.dao.UserDao;
import com.yanhuo.xo.entity.LikeOrCollection;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.FollowerVo;
import com.yanhuo.xo.vo.NoteSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    NoteService noteService;

    @Autowired
    LikeOrCollectionService likeOrCollectionService;

    @Override
    public Page<NoteSearchVo> getTrendPageByUser(long currentPage, long pageSize, String userId, Integer type) {
        Page<NoteSearchVo> resultPage;
        if (type == 1) {
            resultPage = this.getLikeOrCollectionPageByUser(currentPage, pageSize, userId);
        } else {
            resultPage = this.getLikeOrCollectionPageByUser(currentPage, pageSize, userId, type);
        }
        return resultPage;
    }

    private Page<NoteSearchVo> getLikeOrCollectionPageByUser(long currentPage, long pageSize, String userId) {
        Page<NoteSearchVo> noteSearchVoPage = new Page<>();
        // 得到当前用户发布的所有专辑
        String currentUserId = AuthContextHolder.getUserId();
        Page<Note> notePage;
        if (currentUserId.equals(userId)) {
            //是当前用户
            notePage = noteService.page(new Page<>((int) currentPage, (int) pageSize), new QueryWrapper<Note>().eq("uid", userId).orderByDesc("pinned", "update_date"));
        } else {
            notePage = noteService.page(new Page<>((int) currentPage, (int) pageSize), new QueryWrapper<Note>().eq("uid", userId).eq("type", 1).orderByDesc("pinned", "update_date"));
        }
        List<Note> noteList = notePage.getRecords();
        long total = notePage.getTotal();

        // 得到所有用户的信息
        Set<String> uids = noteList.stream().map(Note::getUid).collect(Collectors.toSet());
        Map<String, User> userMap = this.listByIds(uids).stream().collect(Collectors.toMap(User::getId, user -> user));

        List<NoteSearchVo> noteSearchVoList = new ArrayList<>();
        for (Note note : noteList) {
            NoteSearchVo noteSearchVo = ConvertUtils.sourceToTarget(note, NoteSearchVo.class);
            User user = userMap.get(note.getUid());
            noteSearchVo.setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setTime(note.getUpdateDate().getTime());
            if (!currentUserId.equals(userId)) {
                noteSearchVo.setViewCount(null);
            }
            noteSearchVoList.add(noteSearchVo);
        }
        noteSearchVoPage.setRecords(noteSearchVoList);
        noteSearchVoPage.setTotal(total);
        return noteSearchVoPage;
    }


    private Page<NoteSearchVo> getLikeOrCollectionPageByUser(long currentPage, long pageSize, String userId, Integer type) {
        Page<NoteSearchVo> noteSearchVoPage = new Page<>();
        Page<LikeOrCollection> likeOrCollectionPage;
        // 得到当前用户发布的所有图片
        if (type == 2) {
            // 所有点赞图片
            likeOrCollectionPage = likeOrCollectionService.page(new Page<>(currentPage, pageSize), new QueryWrapper<LikeOrCollection>().eq("uid", userId).eq("type", 1).orderByDesc("create_date"));
        } else {
            // 所有收藏图片
            likeOrCollectionPage = likeOrCollectionService.page(new Page<>(currentPage, pageSize), new QueryWrapper<LikeOrCollection>().eq("uid", userId).eq("type", 3).orderByDesc("create_date"));
        }

        List<LikeOrCollection> likeOrCollectionList = likeOrCollectionPage.getRecords();
        long total = likeOrCollectionPage.getTotal();

        Set<String> uids = likeOrCollectionList.stream().map(LikeOrCollection::getPublishUid).collect(Collectors.toSet());
        Map<String, User> userMap = this.listByIds(uids).stream().collect(Collectors.toMap(User::getId, user -> user));

        Set<String> nids = likeOrCollectionList.stream().map(LikeOrCollection::getLikeOrCollectionId).collect(Collectors.toSet());
        Map<String, Note> noteMap = noteService.listByIds(nids).stream().collect(Collectors.toMap(Note::getId, note -> note));

        List<NoteSearchVo> noteSearchVoList = new ArrayList<>();

        for (LikeOrCollection model : likeOrCollectionList) {
            Note note = noteMap.get(model.getLikeOrCollectionId());
            NoteSearchVo noteSearchVo = ConvertUtils.sourceToTarget(note, NoteSearchVo.class);
            User user = userMap.get(model.getPublishUid());
            noteSearchVo.setUsername(user.getUsername())
                    .setAvatar(user.getAvatar());
            noteSearchVoList.add(noteSearchVo);
        }

        noteSearchVoPage.setRecords(noteSearchVoList);
        noteSearchVoPage.setTotal(total);
        return noteSearchVoPage;
    }


    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public Page<FollowerVo> getUserPageByKeyword(long currentPage, long pageSize, String keyword) {
        return null;
    }

    @Override
    public void saveUserSearchRecord(String keyword) {

    }
}
