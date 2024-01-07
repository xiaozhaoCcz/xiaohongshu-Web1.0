package com.yanhuo.platform.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.auth.AuthContextHolder;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.client.EsClient;
import com.yanhuo.platform.service.NoteService;
import com.yanhuo.platform.service.TagNoteRelationService;
import com.yanhuo.platform.service.TagService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.dao.NoteDao;
import com.yanhuo.xo.dto.NoteDTO;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.entity.Tag;
import com.yanhuo.xo.entity.TagNoteRelation;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.NoteSearchVo;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteDao, Note> implements NoteService {
    @Autowired
    UserService userService;

    @Autowired
    TagNoteRelationService tagNoteRelationService;

    @Autowired
    TagService tagService;

    @Autowired
    EsClient esClient;

    @Override
    public Page<NoteVo> getNotePage(long currentPage, long pageSize) {
        return null;
    }

    @Override
    public NoteVo getNoteById(String noteId) {
        Note note = this.getById(noteId);
        note.setViewCount(note.getViewCount() + 1);
        User user = userService.getById(note.getUid());
        NoteVo noteVo = ConvertUtils.sourceToTarget(note, NoteVo.class);
        noteVo.setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setTime(note.getUpdateDate().getTime());

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
    public String saveNoteByDTO(NoteDTO noteDTO) {
        String currentUid = AuthContextHolder.getUserId();
        Note note = ConvertUtils.sourceToTarget(noteDTO, Note.class);
        note.setUid(currentUid);
        String urls = JSONUtil.toJsonStr(noteDTO.getUrls());
        note.setUrls(urls);
        this.save(note);

        //TODO 专辑中需要添加

        User user = userService.getById(currentUid);
        user.setTrendCount(user.getTrendCount() + 1);
        userService.updateById(user);

        // 往es中添加数据
        NoteSearchVo noteSearchVo = ConvertUtils.sourceToTarget(note, NoteSearchVo.class);
        noteSearchVo.setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setLikeCount(0L)
                .setTime(note.getUpdateDate().getTime());

        esClient.addNote(noteSearchVo);
        return note.getId();
    }

    @Override
    public void deleteNoteByIds(List<String> noteIds) {

    }

    @Override
    public String updateNoteByDTO(NoteDTO noteDTO) {
        return null;
    }

    @Override
    public Page<NoteVo> getHotPage(long currentPage, long pageSize) {
        return null;
    }
}
