package com.yanhuo.search.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.search.dto.NoteDTO;
import com.yanhuo.search.utils.model.SearchParam;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.vo.NoteSearchVo;

import java.util.List;

public interface NoteService extends IService<Note> {
    Page<NoteSearchVo> getNotePageByDTO(long currentPage, long pageSize, NoteDTO noteDTO);

    Page<NoteSearchVo> getNoteBySearchParam(SearchParam searchParam);

    Page<NoteSearchVo> getRecommendNotePage(long currentPage, long pageSize);

    void addNote(NoteSearchVo noteSearchVo);

    void addNoteBulkData(List<NoteSearchVo> noteSearchVoList);
}
