package com.yanhuo.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author xiaozhao
 */
@SpringBootApplication(scanBasePackages = {"com.yanhuo.platform","com.yanhuo.common"})
@EnableFeignClients
public class PlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlatformApplication.class, args);
    }
}
