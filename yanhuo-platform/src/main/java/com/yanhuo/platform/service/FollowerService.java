package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.entity.Follower;
import com.yanhuo.xo.vo.FollowerVo;

public interface FollowerService  extends IService<Follower> {
    Page<TrendVo> getFollowTrendPage(long currentPage, long pageSize);

    Page<FollowerVo> getFriendPage(long currentPage, long pageSize, Integer type);

    void followById(String followerId);

    boolean isFollow(String followerId);

    Page<FollowerVo> getNoticeFollower(long currentPage, long pageSize);
}
