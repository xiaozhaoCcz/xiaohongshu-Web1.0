package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.common.validator.group.UpdateGroup;
import com.yanhuo.platform.service.NoteService;
import com.yanhuo.xo.dto.NoteDTO;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/note")
@RestController
public class NoteController {

    @Autowired
    NoteService noteService;


    @RequestMapping("getNotePage/{currentPage}/{pageSize}")
    public Result<?> getNotePage(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<NoteVo> pageInfo = noteService.getNotePage(currentPage, pageSize);
        return Result.ok(pageInfo);
    }


    @RequestMapping("getNoteById")
    public Result<?> getNoteById(String noteId) {
        NoteVo noteVo = noteService.getNoteById(noteId);
        return Result.ok(noteVo);
    }

    @RequestMapping("saveNoteByDTO")
    public Result<?> saveNoteByDTO(@RequestBody NoteDTO noteDTO) {
        ValidatorUtils.validateEntity(noteDTO, AddGroup.class);
        String id = noteService.saveNoteByDTO(noteDTO);
        return Result.ok(id);
    }

    @RequestMapping("deleteNoteByIds")
    public Result<?> deleteNoteByIds(@RequestBody List<String> noteIds) {
        noteService.deleteNoteByIds(noteIds);
        return Result.ok();
    }

    @RequestMapping("updateNoteByDTO")
    public Result<?> updateNoteByDTO(@RequestBody NoteDTO noteDTO) {
        ValidatorUtils.validateEntity(noteDTO, UpdateGroup.class);
        String id = noteService.updateNoteByDTO(noteDTO);
        return Result.ok(id);
    }

    @RequestMapping("getHotPage/{currentPage}/{pageSize}")
    public Result<?> getHotPage(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<NoteVo> pageInfo = noteService.getHotPage(currentPage, pageSize);
        return Result.ok(pageInfo);
    }
}
