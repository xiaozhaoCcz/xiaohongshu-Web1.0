package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.CommentDTO;
import com.yanhuo.xo.entity.Comment;
import com.yanhuo.xo.vo.CommentVo;

import java.util.List;
import java.util.Map;

/**
 * @author xiaozhao
 */
public interface CommentService extends IService<Comment> {
    /**
     * 得到所有的一级分类
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param noteId      笔记id
     * @return 评论结果集
     */
    Page<CommentVo> getOneCommentPageByNoteId(long currentPage, long pageSize, String noteId);

    /**
     * 根据评论id获取当前评论
     *
     * @param commentId 评论id
     * @return 评论实体
     */
    Object getCommentById(String commentId);

    /**
     * 保存评论
     *
     * @param commentDTO 评论实体
     * @return 增加后的评论实体
     */
    CommentVo saveCommentByDTO(CommentDTO commentDTO);

    /**
     * 根据一级评论id获取所有的二级评论
     *
     * @param currentPage  当前页
     * @param pageSize     分页数
     * @param oneCommentId 一级评论id
     * @return 评论结果集
     */
    Page<CommentVo> getTwoCommentPageByOneCommentId(long currentPage, long pageSize, String oneCommentId);

    /**
     * 得到所有的一级评论并携带二级评论
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param noteId      笔记id
     * @return 评论结果集
     */
    Page<CommentVo> getCommentPageWithCommentByNoteId(long currentPage, long pageSize, String noteId);

    /**
     * 自动滚动到当前评论
     *
     * @param commentId 评论id
     * @param noteId    笔记id
     * @return resMap
     */
    Map<String, Object> scrollComment(String commentId);

    /**
     * 删除一条评论
     *
     * @param commentId 评论id
     * @return success
     */
    void deleteCommentById(String commentId);

    /**
     * 根据评论id同步评论集
     *
     * @param commentIds 评论id数据集
     * @return success
     */
    void syncCommentByIds(List<String> commentIds);

    /**
     * 获取当前用户通知的评论集
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return 评论结果集
     */
    IPage<CommentVo> getNoticeComment(long currentPage, long pageSize);
}
