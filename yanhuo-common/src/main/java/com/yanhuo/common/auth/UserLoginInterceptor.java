package com.yanhuo.common.auth;

import com.yanhuo.common.constant.TokenConstant;
import com.yanhuo.common.constant.UserConstant;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.result.ResultCodeEnum;
import com.yanhuo.common.utils.JwtUtils;
import com.yanhuo.common.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaozhao
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    /**
     * token拦截验证
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String accessToken = request.getHeader(TokenConstant.ACCESS_TOKEN);
        log.info("accessToken:{},{}", accessToken,WebUtils.getRequestHeader(UserConstant.USER_ID));
        //判断token不为空
        if (!StringUtils.isEmpty(accessToken)) {
            boolean flag = JwtUtils.checkToken(accessToken);
            if (!flag) {
                throw new YanHuoException(ResultCodeEnum.TOKEN_EXIST.getMessage(), ResultCodeEnum.TOKEN_EXIST.getCode());
            }
            String userId = JwtUtils.getUserId(accessToken);
            AuthContextHolder.setUserId(userId);
            return true;
        }
        throw new YanHuoException(ResultCodeEnum.TOKEN_FAIL.getMessage(), ResultCodeEnum.TOKEN_FAIL.getCode());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        log.info("清除UserID");
        AuthContextHolder.removeUserId();
    }
}
