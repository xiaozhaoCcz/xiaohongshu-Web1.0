package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.CommentDTO;
import com.yanhuo.xo.entity.Comment;
import com.yanhuo.xo.vo.CommentVo;

import java.util.List;
import java.util.Map;

public interface CommentService extends IService<Comment> {

    Page<CommentVo> getOneCommentPageByNoteId(long currentPage, long pageSize, String noteId);

    Object getCommentById(String commentId);

    CommentVo saveCommentByDTO(CommentDTO commentDTO);

    Page<CommentVo> getTwoCommentPageByOneCommentId(long currentPage, long pageSize, String oneCommentId);

    List<CommentVo> getTwoCommentListByOneCommentId(String oneCommentId);

    Page<CommentVo> getReplyCommentPageByUserId(long currentPage, long pageSize);

    /**
     * 得到展示的一级评论和二级评论
     * @param currentPage
     * @param pageSize
     * @param noteId
     * @return
     */
    Page<CommentVo> getCommentPageWithCommentByNoteId(long currentPage, long pageSize, String noteId);

    Map<String, Object> scrollComment(String commentId, String noteId);

    void deleteCommentById(String commentId);

    void syncCommentByIds(List<String> commentIds);

    /**
     * 得到通知的评论
     * @param currentPage
     * @param pageSize
     * @return
     */
    IPage<CommentVo> getNoticeComment(long currentPage, long pageSize);
}
