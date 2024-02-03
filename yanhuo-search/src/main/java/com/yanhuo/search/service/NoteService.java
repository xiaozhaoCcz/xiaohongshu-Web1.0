package com.yanhuo.search.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.search.dto.NoteDTO;
import com.yanhuo.search.utils.model.SearchParam;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.vo.NoteSearchVo;

import java.util.List;

public interface NoteService extends IService<Note> {

    /**
     * 搜索对应的笔记
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param noteDTO     笔记实体
     * @return
     */
    Page<NoteSearchVo> getNotePageByDTO(long currentPage, long pageSize, NoteDTO noteDTO);

    /**
     * 通过参数获取笔记集合
     *
     * @param searchParam 查询参数
     * @return Page<NoteSearchVo>
     */
    Page<NoteSearchVo> getNoteBySearchParam(SearchParam searchParam);

    /**
     * 分页查询笔记
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return Page<NoteSearchVo>
     */
    Page<NoteSearchVo> getRecommendNotePage(long currentPage, long pageSize);

    /**
     * 增加笔记
     *
     * @param noteSearchVo 笔记实体
     */
    void addNote(NoteSearchVo noteSearchVo);

    /**
     * 批量增加笔记
     *
     * @param noteSearchVoList 笔记实体集合
     */
    void addNoteBulkData(List<NoteSearchVo> noteSearchVoList);
}
