package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.FollowerVo;
import com.yanhuo.xo.vo.NoteSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("getTrendPageByUser/{currentPage}/{pageSize}")
    public Result<?> getTrendPageByUser(@PathVariable long currentPage, @PathVariable long pageSize, String userId) {
        Page<NoteSearchVo> pageInfo = userService.getTrendPageByUser(currentPage, pageSize,userId);
        return Result.ok(pageInfo);
    }

    /**
     * 获取用户信息
     *
     * @param uid
     * @return
     */
    @RequestMapping("getUserById")
    public Result<?> getUserById(String userId) {
        User user = userService.getById(userId);
        return Result.ok(user);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @RequestMapping("updateUser")
    public Result<?> updateUser(@RequestBody User user) {
        User updateUser = userService.updateUser(user);
        return Result.ok(updateUser);
    }


    /**
     * 查找用户信息
     *
     * @param keyword
     * @return
     */
    @RequestMapping("getUserPageByKeyword/{currentPage}/{pageSize}")
    public Result<?> getUserPageByKeyword(@PathVariable long currentPage, @PathVariable long pageSize, String keyword) {
        Page<FollowerVo> pageInfo = userService.getUserPageByKeyword(currentPage, pageSize, keyword);
        return Result.ok(pageInfo);
    }
}
