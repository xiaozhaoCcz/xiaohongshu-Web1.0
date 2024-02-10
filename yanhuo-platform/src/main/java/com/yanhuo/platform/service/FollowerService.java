package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.entity.Follower;
import com.yanhuo.xo.vo.FollowerVo;

/**
 * @author xiaozhao
 */
public interface FollowerService  extends IService<Follower> {
    /**
     * 得到关注用户的所有动态
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return 动态
     */
    Page<TrendVo> getFollowTrendPage(long currentPage, long pageSize);

    /**
     * 得到当前用户所有的关注和粉丝
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param type        类型
     * @return FollowerVo
     */
    Page<FollowerVo> getFriendPage(long currentPage, long pageSize, Integer type);

    /**
     * 关注用户
     *
     * @param followerId 关注用户id
     * @return success
     */
    void followById(String followerId);

    /**
     * 当前用户是否关注
     *
     * @param followerId 关注的用户id
     * @return success
     */
    boolean isFollow(String followerId);

    /**
     * 得到当前用户的最新关注信息
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return FollowerVo
     */
    Page<FollowerVo> getNoticeFollower(long currentPage, long pageSize);
}
