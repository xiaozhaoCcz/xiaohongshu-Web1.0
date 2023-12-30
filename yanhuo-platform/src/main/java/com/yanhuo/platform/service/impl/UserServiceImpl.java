package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.dao.UserDao;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.FollowerVo;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Override
    public Page<TrendVo> getTrendPageByUser(long currentPage, long pageSize, Integer type) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public Page<FollowerVo> getUserPageByKeyword(long currentPage, long pageSize, String keyword) {
        return null;
    }
}
