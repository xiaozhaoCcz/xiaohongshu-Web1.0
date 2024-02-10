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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xiaozhao
 */
@RequestMapping("/note")
@RestController
public class NoteController {

    @Autowired
    NoteService noteService;

    /**
     * 根据笔记id获取笔记
     *
     * @param noteId 笔记id
     * @return noteVo
     */
    @RequestMapping("getNoteById")
    public Result<?> getNoteById(String noteId) {
        NoteVo noteVo = noteService.getNoteById(noteId);
        return Result.ok(noteVo);
    }

    /**
     * 保存笔记
     *
     * @param noteDTO 笔记实体
     * @return 笔记id
     */
    @RequestMapping("saveNoteByDTO")
    public Result<?> saveNoteByDTO(@RequestParam("noteData")String noteData, @RequestParam("uploadFiles")MultipartFile[] files) {
        String id = noteService.saveNoteByDTO(noteData,files);
        return Result.ok(id);
    }

    /**
     * 删除笔记
     *
     * @param noteIds 笔记id集合
     * @return success
     */
    @RequestMapping("deleteNoteByIds")
    public Result<?> deleteNoteByIds(@RequestBody List<String> noteIds) {
        noteService.deleteNoteByIds(noteIds);
        return Result.ok();
    }

    /**
     * 更新笔记
     *
     * @param noteDTO 笔记实体
     * @return 笔记id
     */
    @RequestMapping("updateNoteByDTO")
    public Result<?> updateNoteByDTO(@RequestBody NoteDTO noteDTO) {
        ValidatorUtils.validateEntity(noteDTO, UpdateGroup.class);
        String id = noteService.updateNoteByDTO(noteDTO);
        return Result.ok(id);
    }

    /**
     * 获取热门的笔记数据
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return 笔记集合
     */
    @RequestMapping("getHotPage/{currentPage}/{pageSize}")
    public Result<?> getHotPage(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<NoteVo> pageInfo = noteService.getHotPage(currentPage, pageSize);
        return Result.ok(pageInfo);
    }
}
