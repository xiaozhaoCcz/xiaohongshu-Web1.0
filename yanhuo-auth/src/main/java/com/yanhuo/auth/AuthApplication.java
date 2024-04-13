package com.yanhuo.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiaozhao
 */
@SpringBootApplication(scanBasePackages = {"com.yanhuo.auth","com.yanhuo.xo","com.yanhuo.common"})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
