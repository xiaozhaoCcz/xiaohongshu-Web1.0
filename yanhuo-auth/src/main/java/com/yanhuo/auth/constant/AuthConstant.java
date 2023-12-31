package com.yanhuo.auth.constant;

/**
 * @author xiaozhao
 */
public interface AuthConstant {

   long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24;

   long REFRESH_TOKEN_EXPIRATION_TIME = ACCESS_TOKEN_EXPIRATION_TIME * 2;

   String REFRESH_TOKEN_START_TIME = "refreshTokenStartTime:";

   String USER_KEY = "userKey:";

   String USER_INFO = "userInfo";

   String CODE = "code:";

   String DEFAULT_AVATAR = "https://foruda.gitee.com/avatar/1677084428450863653/7573881_xzjsccz_1604058944.png!avatar200";

   String DEFAULT_COVER = "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/06/02/c6a167251a194484ac7b25c5e3ae366720200725103959_K8EJa.jpeg";

   String DEFAULT_PASSWORD = "qwer1234";

   String LOGIN_FAIL = "登录失败";
}
