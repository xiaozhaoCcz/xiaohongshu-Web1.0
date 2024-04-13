package com.yanhuo.search.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.myVaildator.noLogin.NoLoginIntercept;
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
@RequestMapping("/note")
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
    @NoLoginIntercept
    @PostMapping("getNotePageByDTO/{currentPage}/{pageSize}")
    public Result<?> getNotePageByDTO(@PathVariable long currentPage, @PathVariable long pageSize, @RequestBody NoteDTO noteDTO) {
        Page<NoteSearchVo> page = noteService.getNotePageByDTO(currentPage, pageSize, noteDTO);
        return Result.ok(page);
    }

    /**
     * 分页查询笔记
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return Page<NoteSearchVo>
     */
    @NoLoginIntercept
    @GetMapping("getRecommendNotePage/{currentPage}/{pageSize}")
    public Result<?> getRecommendNotePage(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<NoteSearchVo> page = noteService.getRecommendNotePage(currentPage, pageSize);
        return Result.ok(page);
    }

    /**
     * 增加笔记
     *
     * @param noteSearchVo 笔记实体
     */
    @PostMapping("addNote")
    public void addNote(@RequestBody NoteSearchVo noteSearchVo) {
        noteService.addNote(noteSearchVo);
    }


    /**
     * 修改笔记
     *
     * @param noteSearchVo 笔记实体
     */
    @PostMapping("updateNote")
    public void updateNote(@RequestBody NoteSearchVo noteSearchVo) {
        noteService.updateNote(noteSearchVo);
    }

    /**
     * 批量增加笔记
     *
     * @param noteSearchVoList 笔记实体集合
     */
    @PostMapping("addNoteBulkData")
    @NoLoginIntercept
    public void addNoteBulkData(@RequestBody List<NoteSearchVo> noteSearchVoList) {
        noteService.addNoteBulkData(noteSearchVoList);
    }

    /**
     * 删除es中的笔记
     *
     * @param noteId
     */
    @RequestMapping("deleteNote/{noteId}")
    public void deleteNote(@PathVariable String noteId) {
        noteService.deleteNote(noteId);
    }
}
