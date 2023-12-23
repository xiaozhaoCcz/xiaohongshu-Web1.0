package com.yanhuo.util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.yanhuo.util","com.yanhuo.xo","com.yanhuo.common"})
public class UtilApplication {
    public static void main(String[] args) {
        SpringApplication.run(UtilApplication.class, args);
    }
}
