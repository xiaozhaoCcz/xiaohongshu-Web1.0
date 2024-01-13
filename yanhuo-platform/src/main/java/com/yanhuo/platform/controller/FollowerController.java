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

import java.util.List;

@RequestMapping("/follower")
@RestController
public class FollowerController {

    @Autowired
    FollowerService followerService;


    @GetMapping("getFollowTrendPage/{currentPage}/{pageSize}")
    public Result<?> getFollowTrendPage(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<TrendVo> pageInfo = followerService.getFollowTrendPage(currentPage, pageSize);
        return Result.ok(pageInfo);
    }


    @RequestMapping("getFriendPage/{currentPage}/{pageSize}")
    public Result<?> getFriendPage(@PathVariable long currentPage, @PathVariable long pageSize, Integer type) {
        Page<FollowerVo> pageInfo = followerService.getFriendPage(currentPage, pageSize, type);
        return Result.ok(pageInfo);
    }


    @RequestMapping("followById")
    public Result<?> followById(String followerId) {
        followerService.followById(followerId);
        return Result.ok();
    }


    @RequestMapping("isFollow")
    public Result<?> isFollow(String followerId) {
        boolean flag = followerService.isFollow(followerId);
        return Result.ok(flag);
    }

    @RequestMapping("getNoticeFollower/{currentPage}/{pageSize}")
    public Result<?> getNoticeFollower(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<FollowerVo> pageInfo = followerService.getNoticeFollower(currentPage, pageSize);
        return Result.ok(pageInfo);
    }
}
