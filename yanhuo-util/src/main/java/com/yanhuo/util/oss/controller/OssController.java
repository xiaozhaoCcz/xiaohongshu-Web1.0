package com.yanhuo.util.oss.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.util.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xiaozhao
 */
@RequestMapping("/oss")
@RestController
public class OssController {

    @Autowired
    OssService ossService;

    /**
     * 上传文件
     * @param file
     * @param type
     * @return
     */
    @PostMapping("save/{type}")
    public Result<?> save(MultipartFile file, @PathVariable Integer type){
        String path = ossService.save(file, type);
        return Result.ok(path);
    }

    /**
     * 批量上传文件
     * @param files
     * @param type 类型
     * @return
     */
    @PostMapping(value = "saveBatch/{type}")
    public Result<List<String>> saveBatch(@RequestParam("uploadFiles") MultipartFile[] files, @PathVariable Integer type) {
        if (files.length == 0) {
            return Result.fail(null);
        }
        List<String> stringList = ossService.saveBatch(files, type);
        return Result.ok(stringList);
    }

    /**
     * 删除文件
     * @param path
     * @param type
     * @return
     */
    @GetMapping("delete/{type}")
    public Result<?> delete(String path, @PathVariable Integer type){
        ossService.delete(path, type);
        return Result.ok();
    }
}
