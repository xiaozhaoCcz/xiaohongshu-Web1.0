package com.yanhuo.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileWriter;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

@SpringBootTest(classes = UtilApplication.class)
public class UtilApplicationTests {

    @Test
    public void test() {
        BufferedInputStream in = FileUtil.getInputStream("/Users/zhaowenpeng/Desktop/title.png");
        BufferedOutputStream out = FileUtil.getOutputStream("/Users/zhaowenpeng/Desktop/title2.png");
        long copySize = IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE);
        System.out.println(11);
    }

}
