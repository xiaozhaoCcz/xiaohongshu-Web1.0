package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.AlbumNoteRelationService;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/albumNoteRelation")
@RestController
public class AlbumNoteRelationController {

    @Autowired
    AlbumNoteRelationService albumNoteRelationService;

    @RequestMapping("getNotePageByAid/{currentPage}/{pageSize}")
    public Result<?> getNotePageByAlbumId(@PathVariable long currentPage, @PathVariable long pageSize, String albumId, Integer type) {
        Page<NoteVo> pageInfo = albumNoteRelationService.getNotePageByAlbumId(currentPage, pageSize, albumId, type);
        return Result.ok(pageInfo);
    }
}
