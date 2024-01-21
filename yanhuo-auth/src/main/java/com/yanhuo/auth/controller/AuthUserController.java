package com.yanhuo.auth.controller;


import com.yanhuo.auth.dto.AuthUserDTO;
import com.yanhuo.auth.service.AuthUserService;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.result.ResultCodeEnum;
import com.yanhuo.common.utils.JwtUtils;
import com.yanhuo.xo.entity.User;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xiaozhao
 */
@Api(tags = "权限模块")
@RestController
@RequestMapping("/auth/auth")
@Slf4j
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    /**
     * 用户登录
     *
     * @param authUserDTO
     * @return
     */
    @PostMapping("login")
    public Result<?> login(@RequestBody AuthUserDTO authUserDTO) {
        Map<String, Object> map = authUserService.login(authUserDTO);
        return Result.ok(map);
    }

    /**
     * 使用验证码登录
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("loginByCode")
    public Result<?> loginByCode(@RequestBody AuthUserDTO authUserDTO) {
        Map<String, Object> map = authUserService.loginByCode(authUserDTO);
        return Result.ok(map);
    }


    /**
     * 根据用户的token信息得到当前用户
     *
     * @param accessToken accessToken
     * @return 用户类
     */
    @RequestMapping("getUserInfoByToken")
    public Result<?> getUserInfoByToken(String accessToken) {
        boolean cheackToken = JwtUtils.checkToken(accessToken);

        if(!cheackToken){
            //通过返回码 告诉客户端 accessToken过期了，需要调用刷新accessToken的接口
            return Result.build(ResultCodeEnum.TOKEN_EXIST.getCode(),ResultCodeEnum.TOKEN_EXIST.getMessage());
        }
        User user = authUserService.getUserInfoByToken(accessToken);
        return Result.ok(user);
    }


    /**
     * 用户注册
     *
     * @param authUserDTO 前台传递用户信息
     */
    @RequestMapping("register")
    public Result<?> register(@RequestBody AuthUserDTO authUserDTO) {
        Map<String, Object> data = authUserService.register(authUserDTO);
        return Result.ok(data);
    }

    /**
     * 用户是否注册
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("isRegister")
    public Result<?> isRegister(@RequestBody AuthUserDTO authUserDTO) {
        boolean flag = authUserService.isRegister(authUserDTO);
        return Result.ok(flag);

    }


    /**
     * 退出登录
     *
     * @param userId
     * @return
     */
    @RequestMapping("loginOut")
    public Result<?> loginOut(String userId) {
        authUserService.loginOut(userId);
        return Result.ok();
    }


    /**
     * 修改密码
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("updatePassword")
    public Result<?> updatePassword(@RequestBody AuthUserDTO authUserDTO) {
        Boolean flag = authUserService.updatePassword(authUserDTO);
        return Result.ok(flag);
    }


    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    @RequestMapping("refreshToken")
    public Result<?> refreshToken(String refreshToken) {
        boolean cheackToken = JwtUtils.checkToken(refreshToken);
        if(!cheackToken){
            //通过返回码 告诉客户端 refreshToken过期了，需要客户端就得跳转登录界面
            return Result.build(ResultCodeEnum.TOKEN_FAIL.getCode(),ResultCodeEnum.TOKEN_FAIL.getMessage());
        }
        Map<String, Object> map = authUserService.refreshToken(refreshToken);
        return Result.ok(map);
    }
}
