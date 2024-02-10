package com.yanhuo.search.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.search.dto.NoteDTO;
import com.yanhuo.search.service.NoteService;
import com.yanhuo.xo.vo.NoteSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiaozhao
 */
@RestController
@RequestMapping("/search/note")
public class NoteController {

    @Autowired
    NoteService noteService;

    /**
     * 搜索对应的笔记
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param noteDTO     笔记实体
     * @return
     */
    @RequestMapping("getNotePageByDTO/{currentPage}/{pageSize}")
    public Result<?> getNotePageByDTO(@PathVariable long currentPage, @PathVariable long pageSize, @RequestBody NoteDTO noteDTO) {
        Page<NoteSearchVo> page = noteService.getNotePageByDTO(currentPage, pageSize, noteDTO);
        return Result.ok(page);
    }

    /**
     * 分页查询笔记
     * @param currentPage 当前页
     * @param pageSize 分页数
     * @return Page<NoteSearchVo>
     */
    @RequestMapping("getRecommendNotePage/{currentPage}/{pageSize}")
    public Result<?> getRecommendNotePage(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<NoteSearchVo> page = noteService.getRecommendNotePage(currentPage, pageSize);
        return Result.ok(page);
    }

    /**
     * 增加笔记
     *
     * @param noteSearchVo 笔记实体
     */
    @RequestMapping("addNote")
    public void addNote(@RequestBody NoteSearchVo noteSearchVo) {
        noteService.addNote(noteSearchVo);
    }

    /**
     * 批量增加笔记
     *
     * @param noteSearchVoList 笔记实体集合
     */
    @RequestMapping("addNoteBulkData")
    public void addNoteBulkData(@RequestBody List<NoteSearchVo> noteSearchVoList) {
        noteService.addNoteBulkData(noteSearchVoList);
    }
}
