package com.yanhuo.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiaozhao
 */
@SpringBootApplication(scanBasePackages = {"com.yanhuo.search","com.yanhuo.xo","com.yanhuo.common"})
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }
}
