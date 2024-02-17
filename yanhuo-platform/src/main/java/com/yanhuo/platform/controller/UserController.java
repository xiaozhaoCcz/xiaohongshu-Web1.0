package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.FollowerVo;
import com.yanhuo.xo.vo.NoteSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaozhao
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 得到当前用户的动态
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param userId      用户id
     * @param type        类型
     * @return Page<NoteSearchVo>
     */
    @GetMapping("getTrendPageByUser/{currentPage}/{pageSize}")
    public Result<?> getTrendPageByUser(@PathVariable long currentPage, @PathVariable long pageSize, String userId, Integer type) {
        Page<NoteSearchVo> pageInfo = userService.getTrendPageByUser(currentPage, pageSize, userId, type);
        return Result.ok(pageInfo);
    }


    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return user
     */
    @GetMapping("getUserById")
    public Result<?> getUserById(String userId) {
        User user = userService.getById(userId);
        return Result.ok(user);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户实体
     * @return user
     */
    @PostMapping("updateUser")
    public Result<?> updateUser(@RequestBody User user) {
        User updateUser = userService.updateUser(user);
        return Result.ok(updateUser);
    }


    /**
     * 查找用户信息
     *
     * @param keyword 关键词
     * @return FollowerVo
     */
    @GetMapping("getUserPageByKeyword/{currentPage}/{pageSize}")
    public Result<?> getUserPageByKeyword(@PathVariable long currentPage, @PathVariable long pageSize, String keyword) {
        Page<FollowerVo> pageInfo = userService.getUserPageByKeyword(currentPage, pageSize, keyword);
        return Result.ok(pageInfo);
    }

    /**
     * 保存用户的搜索记录
     *
     * @param keyword 关键词
     * @return success
     */
    @GetMapping("saveUserSearchRecord")
    public Result<?> saveUserSearchRecord(String keyword) {
        userService.saveUserSearchRecord(keyword);
        return Result.ok();
    }
}
