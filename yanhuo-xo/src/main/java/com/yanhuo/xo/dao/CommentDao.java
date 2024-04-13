package com.yanhuo.xo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhuo.xo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaozhao
 */
@Mapper
public interface CommentDao extends BaseMapper<Comment> {
}
