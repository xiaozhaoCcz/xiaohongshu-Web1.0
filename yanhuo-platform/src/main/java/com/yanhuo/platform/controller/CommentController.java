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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author xiaozhao
 */
@RequestMapping("/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 得到所有的一级分类
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param noteId      笔记id
     * @return 评论结果集
     */
    @GetMapping("getOneCommentPageByNoteId/{currentPage}/{pageSize}")
    public Result<?> getOneCommentPageByNoteId(@PathVariable long currentPage, @PathVariable long pageSize, String noteId) {
        Page<CommentVo> pageInfo = commentService.getOneCommentPageByNoteId(currentPage, pageSize, noteId);
        return Result.ok(pageInfo);
    }

    /**
     * 根据评论id获取当前评论
     *
     * @param commentId 评论id
     * @return 评论实体
     */
    @GetMapping("getCommentById")
    public Result<?> getCommentById(String commentId) {
        return Result.ok(commentService.getCommentById(commentId));
    }

    /**
     * 保存评论
     *
     * @param commentDTO 评论实体
     * @return 增加后的评论实体
     */
    @PostMapping("saveCommentByDTO")
    public Result<?> saveCommentByDTO(@RequestBody CommentDTO commentDTO) {
//        ValidatorUtils.validateEntity(commentDTO, AddGroup.class);
        CommentVo commentVo = commentService.saveCommentByDTO(commentDTO);
        return Result.ok(commentVo);
    }

    /**
     * 根据评论id同步评论集
     *
     * @param commentIds 评论id数据集
     * @return success
     */
    @PostMapping("syncCommentByIds")
    public Result<?> syncCommentByIds(@RequestBody List<String> commentIds) {
        commentService.syncCommentByIds(commentIds);
        return Result.ok();
    }

    /**
     * 根据一级评论id获取所有的二级评论
     *
     * @param currentPage  当前页
     * @param pageSize     分页数
     * @param oneCommentId 一级评论id
     * @return 评论结果集
     */
    @GetMapping("getTwoCommentPageByOneCommentId/{currentPage}/{pageSize}")
    public Result<?> getTwoCommentPageByOneCommentId(@PathVariable long currentPage, @PathVariable long pageSize, String oneCommentId) {
        IPage<CommentVo> pageInfo = commentService.getTwoCommentPageByOneCommentId(currentPage, pageSize, oneCommentId);
        return Result.ok(pageInfo);
    }

    /**
     * 获取当前用户通知的评论集
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return 评论结果集
     */
    @GetMapping("getNoticeComment/{currentPage}/{pageSize}")
    public Result<?> getNoticeComment(@PathVariable long currentPage, @PathVariable long pageSize) {
        IPage<CommentVo> pageInfo = commentService.getNoticeComment(currentPage, pageSize);
        return Result.ok(pageInfo);
    }


    /**
     * 得到所有的一级评论并携带二级评论
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param noteId      笔记id
     * @return 评论结果集
     */
    @GetMapping("getCommentPageWithCommentByNoteId/{currentPage}/{pageSize}")
    public Result<?> getCommentPageWithCommentByNoteId(@PathVariable long currentPage, @PathVariable long pageSize, String noteId) {
        Page<CommentVo> pageInfo = commentService.getCommentPageWithCommentByNoteId(currentPage, pageSize, noteId);
        return Result.ok(pageInfo);

    }

    /**
     * 自动滚动到当前评论
     *
     * @param commentId 评论id
     * @return resMap
     */
    @GetMapping("scrollComment")
    public Result<?> scrollComment(String commentId) {
        Map<String, Object> resMap = commentService.scrollComment(commentId);
        return Result.ok(resMap);
    }

    /**
     * 删除一条评论
     *
     * @param commentId 评论id
     * @return success
     */
    @GetMapping("deleteCommentById")
    public Result<?> deleteCommentById(String commentId) {
        commentService.deleteCommentById(commentId);
        return Result.ok();
    }
}
