package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.CommentService;
import com.yanhuo.xo.dao.CommentDao;
import com.yanhuo.xo.entity.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {
}
