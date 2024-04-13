package com.yanhuo.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.auth.dto.AuthUserDTO;
import com.yanhuo.xo.entity.User;

import java.util.Map;

/**
 * @author xiaozhao
 */
public interface AuthUserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param authUserDTO
     * @return
     */
    Map<String, Object> login(AuthUserDTO authUserDTO);

    /**
     * 使用验证码登录
     *
     * @param authUserDTO
     * @return
     */
    Map<String, Object> loginByCode(AuthUserDTO authUserDTO);

    /**
     * 根据用户的token信息得到当前用户
     *
     * @param accessToken token信息
     * @return 用户类
     */
    User getUserInfoByToken(String accessToken);

    /**
     * 用户注册
     *
     * @param authUserDTO 前台传递用户信息
     */
    Map<String, Object> register(AuthUserDTO authUserDTO);

    /**
     * 用户是否注册
     *
     * @param authUserDTO
     * @return
     */
    boolean isRegister(AuthUserDTO authUserDTO);

    /**
     * 退出登录
     *
     * @param userId
     * @return
     */
    void loginOut(String userId);

    /**
     * 修改密码
     *
     * @param authUserDTO
     * @return
     */
    boolean updatePassword(AuthUserDTO authUserDTO);

    /**
     * 第三方登录
     *
     * @param userOtherLoginRelationDTO
     * @return
     */
    // @Deprecated
    // Map<String, Object> otherLogin(UserOtherLoginRelationDTO userOtherLoginRelationDTO);

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    Map<String, Object> refreshToken(String refreshToken);
}
