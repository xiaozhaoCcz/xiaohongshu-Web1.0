package com.yanhuo.common.auth;


/**
 * @author xiaozhao
 */
public class AuthContextHolder {
    private AuthContextHolder() {
    }

    //用户id
    private static ThreadLocal<String> userId = new ThreadLocal<>();

    //userId操作的方法
    public static void setUserId(String _userId) {
        userId.set(_userId);
    }


    public static String getUserId() {
        return userId.get();
    }

    public static void removeUserId() {
        userId.remove();
    }
}
