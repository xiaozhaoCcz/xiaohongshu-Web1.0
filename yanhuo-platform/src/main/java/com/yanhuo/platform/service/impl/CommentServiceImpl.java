package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.CommentService;
import com.yanhuo.xo.dao.CommentDao;
import com.yanhuo.xo.dto.CommentDTO;
import com.yanhuo.xo.entity.Comment;
import com.yanhuo.xo.vo.CommentVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {
    @Override
    public Page<CommentVo> getOneCommentPageByNoteId(long currentPage, long pageSize, String noteId) {
        return null;
    }

    @Override
    public Object getCommentById(String commentId) {
        return null;
    }

    @Override
    public CommentVo saveCommentByDTO(CommentDTO commentDTO) {
        return null;
    }

    @Override
    public IPage<CommentVo> getTwoCommentPageByOneCommentId(long currentPage, long pageSize, String oneCommentId) {
        return null;
    }

    @Override
    public List<CommentVo> getTwoCommentListByOneCommentId(String oneCommentId) {
        return null;
    }

    @Override
    public Page<CommentVo> getReplyCommentPageByUserId(long currentPage, long pageSize) {
        return null;
    }

    @Override
    public Page<CommentVo> getCommentPageWithCommentByNoteId(long currentPage, long pageSize, String noteId) {
        return null;
    }

    @Override
    public Map<String, Object> scrollComment(String commentId, String noteId) {
        return null;
    }

    @Override
    public void deleteCommentById(String commentId) {

    }
}
