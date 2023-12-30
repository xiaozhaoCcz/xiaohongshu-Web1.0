package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.platform.service.FollowerService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.dto.FollowDTO;
import com.yanhuo.xo.vo.FollowerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/follower")
@RestController
public class FollowerController {

    @Autowired
    FollowerService followerService;


    @RequestMapping("getFollowTrendPage/{currentPage}/{pageSize}")
    public Result<?> getFollowTrendPage(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<TrendVo> pageInfo = followerService.getFollowTrendPage(currentPage, pageSize);
        return Result.ok(pageInfo);
    }


    /**
     * 根据类型获取所有关注和粉丝
     *
     * @param type
     * @return
     */
    @RequestMapping("getFriendPage/{currentPage}/{pageSize}")
    public Result<?> getFriendPage(@PathVariable long currentPage, @PathVariable long pageSize, Integer type) {
        Page<FollowerVo> pageInfo = followerService.getFriendPage(currentPage, pageSize, type);
        return Result.ok(pageInfo);
    }

    /**
     * 关注用户
     *
     * @param followDTO
     * @return
     */
    @RequestMapping("followById")
    public Result<?> followById(String followerId) {
        followerService.followById(followerId);
        return Result.ok(null);
    }

    /**
     * 查看是否关注用户
     *
     * @param uid
     * @param fid
     * @return
     */
    @RequestMapping("isFollow")
    public Result<?> isFollow(String followerId) {
        boolean flag = followerService.isFollow(followerId);
        return Result.ok(flag);
    }

    /**
     * 删除关注
     *
     * @param followDTO
     * @return
     */
    @RequestMapping("deleteFollowerById")
    public Result<?> deleteFollowerById(String followerId) {
        followerService.deleteFollowerById(followerId);
        return Result.ok(null);
    }
}
