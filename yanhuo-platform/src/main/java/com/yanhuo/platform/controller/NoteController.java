package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.myVaildator.noLogin.NoLoginIntercept;
import com.yanhuo.platform.service.NoteService;
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
    @GetMapping("getNoteById")
    @NoLoginIntercept
    public Result<?> getNoteById(String noteId) {
        NoteVo noteVo = noteService.getNoteById(noteId);
        return Result.ok(noteVo);
    }

    /**
     * 保存笔记
     *
     * @param noteData 笔记对象
     * @param files    图片文件
     * @return
     */
    @PostMapping("saveNoteByDTO")
    public Result<?> saveNoteByDTO(@RequestParam("noteData") String noteData, @RequestParam("uploadFiles") MultipartFile[] files) {
        String id = noteService.saveNoteByDTO(noteData, files);
        return Result.ok(id);
    }

    /**
     * 删除笔记
     *
     * @param noteIds 笔记id集合
     * @return success
     */
    @PostMapping("deleteNoteByIds")
    public Result<?> deleteNoteByIds(@RequestBody List<String> noteIds) {
        noteService.deleteNoteByIds(noteIds);
        return Result.ok();
    }

    /**
     * 更新笔记
     *
     * @param noteData 笔记对象
     * @param files    图片文件
     */
    @PostMapping("updateNoteByDTO")
    public Result<?> updateNoteByDTO(@RequestParam("noteData") String noteData, @RequestParam("uploadFiles") MultipartFile[] files) {
        noteService.updateNoteByDTO(noteData, files);
        return Result.ok();
    }

    /**
     * 获取热门的笔记数据
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return 笔记集合
     */
    @GetMapping("getHotPage/{currentPage}/{pageSize}")
    public Result<?> getHotPage(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<NoteVo> pageInfo = noteService.getHotPage(currentPage, pageSize);
        return Result.ok(pageInfo);
    }

    /**
     * 置顶笔记
     *
     * @param noteId 笔记id
     * @return
     */
    @GetMapping("pinnedNote")
    public Result<?> pinnedNote(String noteId) {
        boolean flag = noteService.pinnedNote(noteId);
        return Result.ok(flag);
    }
}
