package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.FollowerService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.dao.FollowerDao;
import com.yanhuo.xo.entity.Follower;
import com.yanhuo.xo.vo.FollowerVo;
import org.springframework.stereotype.Service;

@Service
public class FollowerServiceImpl extends ServiceImpl<FollowerDao, Follower> implements FollowerService {
    @Override
    public Page<TrendVo> getFollowTrendPage(long currentPage, long pageSize) {
        return null;
    }

    @Override
    public Page<FollowerVo> getFriendPage(long currentPage, long pageSize, Integer type) {
        return null;
    }

    @Override
    public void followById(String followerId) {

    }

    @Override
    public boolean isFollow(String followerId) {
        return false;
    }

    @Override
    public void deleteFollowerById(String followerId) {

    }
}
