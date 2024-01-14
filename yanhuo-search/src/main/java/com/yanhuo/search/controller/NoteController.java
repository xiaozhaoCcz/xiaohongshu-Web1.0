package com.yanhuo.search.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.search.dto.NoteDTO;
import com.yanhuo.search.service.NoteService;
import com.yanhuo.xo.vo.NoteSearchVo;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author xiaozhao
 */
@RestController
@RequestMapping("/search/note")
public class NoteController {

    @Autowired
    NoteService noteService;


    @RequestMapping("getNotePageByDTO/{currentPage}/{pageSize}")
    public Result<?> getNotePageByDTO(@PathVariable long currentPage, @PathVariable long pageSize,@RequestBody NoteDTO noteDTO){
        Page<NoteSearchVo> page = noteService.getNotePageByDTO(currentPage,pageSize,noteDTO);
        return Result.ok(page);
    }


    @RequestMapping("getRecommendNotePage/{currentPage}/{pageSize}")
    public Result<?> getRecommendNotePage(@PathVariable long currentPage, @PathVariable long pageSize){
        Page<NoteSearchVo> page = noteService.getRecommendNotePage(currentPage,pageSize);
        return Result.ok(page);
    }

    @RequestMapping("addNote")
    public void addNote(@RequestBody NoteSearchVo noteSearchVo){
        noteService.addNote(noteSearchVo);
    }

    @RequestMapping("addNoteBulkData")
    public void addNoteBulkData(@RequestBody List<NoteSearchVo> noteSearchVoList){
        noteService.addNoteBulkData(noteSearchVoList);
    }
}
