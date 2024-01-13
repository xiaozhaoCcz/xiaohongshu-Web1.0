package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.platform.service.CommentService;
import com.yanhuo.xo.dto.CommentDTO;
import com.yanhuo.xo.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping("getOneCommentPageByNoteId/{currentPage}/{pageSize}")
    public Result<?> getOneCommentPageByNoteId(@PathVariable long currentPage, @PathVariable long pageSize, String noteId) {
        Page<CommentVo> pageInfo = commentService.getOneCommentPageByNoteId(currentPage, pageSize, noteId);
        return Result.ok(pageInfo);
    }

    @RequestMapping("getCommentById")
    public Result<?> getCommentById(String commentId) {
        return Result.ok(commentService.getCommentById(commentId));
    }


    @RequestMapping("saveCommentByDTO")
    public Result<?> saveCommentByDTO(@RequestBody CommentDTO commentDTO) {
//        ValidatorUtils.validateEntity(commentDTO, AddGroup.class);
        CommentVo commentVo = commentService.saveCommentByDTO(commentDTO);
        return Result.ok(commentVo);
    }

    @RequestMapping("syncCommentByIds")
    public Result<?> syncCommentByIds(@RequestBody List<String> commentIds) {
        commentService.syncCommentByIds(commentIds);
        return Result.ok();
    }

    @RequestMapping("getTwoCommentPageByOneCommentId/{currentPage}/{pageSize}")
    public Result<?> getTwoCommentPageByOneCommentId(@PathVariable long currentPage, @PathVariable long pageSize, String oneCommentId) {
        IPage<CommentVo> pageInfo = commentService.getTwoCommentPageByOneCommentId(currentPage, pageSize, oneCommentId);
        return Result.ok(pageInfo);
    }


    @RequestMapping("getNoticeComment/{currentPage}/{pageSize}")
    public Result<?> getNoticeComment(@PathVariable long currentPage, @PathVariable long pageSize) {
        IPage<CommentVo> pageInfo = commentService.getNoticeComment(currentPage, pageSize);
        return Result.ok(pageInfo);
    }

    @RequestMapping("getTwoCommentListByOneCommentId")
    public Result<?> getTwoCommentListByOneCommentId(String oneCommentId) {
        List<CommentVo> pageInfo = commentService.getTwoCommentListByOneCommentId(oneCommentId);
        return Result.ok(pageInfo);
    }

    @RequestMapping("getReplyCommentPageByUserId/{currentPage}/{pageSize}")
    public Result<?> getReplyCommentPageByUserId(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<CommentVo> pageInfo = commentService.getReplyCommentPageByUserId(currentPage, pageSize);
        return Result.ok(pageInfo);
    }


    @RequestMapping("getCommentPageWithCommentByNoteId/{currentPage}/{pageSize}")
    public Result<?> getCommentPageWithCommentByNoteId(@PathVariable long currentPage, @PathVariable long pageSize, String noteId) {
        Page<CommentVo> pageInfo = commentService.getCommentPageWithCommentByNoteId(currentPage, pageSize, noteId);
        return Result.ok(pageInfo);

    }

    @RequestMapping("scrollComment")
    public Result<?> scrollComment(String commentId, String noteId){
        Map<String,Object> resMap= commentService.scrollComment(commentId, noteId);
        return Result.ok(resMap);
    }

    @RequestMapping("deleteCommentById")
    public Result<?> deleteCommentById(String commentId) {
        commentService.deleteCommentById(commentId);
        return Result.ok();
    }
}
