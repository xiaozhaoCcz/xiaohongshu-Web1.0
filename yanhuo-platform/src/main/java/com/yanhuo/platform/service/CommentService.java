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

    IPage<CommentVo> getTwoCommentPageByOneCommentId(long currentPage, long pageSize, String oneCommentId);

    List<CommentVo> getTwoCommentListByOneCommentId(String oneCommentId);

    Page<CommentVo> getReplyCommentPageByUserId(long currentPage, long pageSize);

    Page<CommentVo> getCommentPageWithCommentByNoteId(long currentPage, long pageSize, String noteId);

    Map<String, Object> scrollComment(String commentId, String noteId);

    void deleteCommentById(String commentId);
}
