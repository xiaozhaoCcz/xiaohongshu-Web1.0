package com.yanhuo.util.oss.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.util.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/util/oss")
@RestController
public class OssController {

    @Autowired
    OssService ossService;

    @RequestMapping("save/{type}")
    public Result<?> save(MultipartFile file, @PathVariable Integer type){
        String path = ossService.save(file, type);
        return Result.ok(path);
    }

    @RequestMapping("saveBatch/{type}")
    public Result<?> saveBatch(@RequestParam("uploadFiles") MultipartFile[] files, @PathVariable Integer type){
        System.out.println(files.length);
        List<String> stringList = ossService.saveBatch(files, type);
        System.out.println(stringList);
        return Result.ok(stringList);
    }
}
