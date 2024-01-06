package com.yanhuo.util.oss.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.util.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
