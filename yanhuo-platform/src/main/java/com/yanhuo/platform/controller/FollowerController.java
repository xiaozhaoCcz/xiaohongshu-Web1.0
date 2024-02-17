package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.FollowerService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.vo.FollowerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaozhao
 */
@RequestMapping("/follower")
@RestController
public class FollowerController {

    @Autowired
    FollowerService followerService;

    /**
     * 得到关注用户的所有动态
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return 动态
     */
    @GetMapping("getFollowTrendPage/{currentPage}/{pageSize}")
    public Result<?> getFollowTrendPage(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<TrendVo> pageInfo = followerService.getFollowTrendPage(currentPage, pageSize);
        return Result.ok(pageInfo);
    }

    /**
     * 得到当前用户所有的关注和粉丝
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param type        类型
     * @return FollowerVo
     */
    @GetMapping("getFriendPage/{currentPage}/{pageSize}")
    public Result<?> getFriendPage(@PathVariable long currentPage, @PathVariable long pageSize, Integer type) {
        Page<FollowerVo> pageInfo = followerService.getFriendPage(currentPage, pageSize, type);
        return Result.ok(pageInfo);
    }

    /**
     * 关注用户
     *
     * @param followerId 关注用户id
     * @return success
     */
    @GetMapping("followById")
    public Result<?> followById(String followerId) {
        followerService.followById(followerId);
        return Result.ok();
    }

    /**
     * 当前用户是否关注
     *
     * @param followerId 关注的用户id
     * @return success
     */
    @GetMapping("isFollow")
    public Result<?> isFollow(String followerId) {
        boolean flag = followerService.isFollow(followerId);
        return Result.ok(flag);
    }

    /**
     * 得到当前用户的最新关注信息
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return FollowerVo
     */
    @GetMapping("getNoticeFollower/{currentPage}/{pageSize}")
    public Result<?> getNoticeFollower(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<FollowerVo> pageInfo = followerService.getNoticeFollower(currentPage, pageSize);
        return Result.ok(pageInfo);
    }
}
