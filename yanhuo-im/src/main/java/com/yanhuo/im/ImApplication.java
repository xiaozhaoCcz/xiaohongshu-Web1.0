package com.yanhuo.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiaozhao
 */
@SpringBootApplication(scanBasePackages = {"com.yanhuo.im","com.yanhuo.xo","com.yanhuo.common"})
public class ImApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImApplication.class, args);
    }
}
