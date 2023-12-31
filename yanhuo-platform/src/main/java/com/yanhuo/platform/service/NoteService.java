package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.NoteDTO;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.vo.NoteVo;

import java.util.List;

public interface NoteService extends IService<Note> {
    Page<NoteVo> getNotePage(long currentPage, long pageSize);

    NoteVo getNoteById(String noteId);

    String saveNoteByDTO(NoteDTO noteDTO);

    void deleteNoteByIds(List<String> noteIds);

    String updateNoteByDTO(NoteDTO noteDTO);

    Page<NoteVo> getHotPage(long currentPage, long pageSize);
}
