package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.NoteService;
import com.yanhuo.xo.dao.NoteDao;
import com.yanhuo.xo.dto.NoteDTO;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteDao, Note> implements NoteService {
    @Override
    public Page<NoteVo> getNotePage(long currentPage, long pageSize) {
        return null;
    }

    @Override
    public NoteVo getNoteById(String noteId) {
        return null;
    }

    @Override
    public String saveNoteByDTO(NoteDTO noteDTO) {
        return null;
    }

    @Override
    public void deleteNoteByIds(List<String> noteIds) {

    }

    @Override
    public String updateImgDetail(NoteDTO noteDTO) {
        return null;
    }

    @Override
    public Page<NoteVo> getHotPage(long currentPage, long pageSize) {
        return null;
    }
}
