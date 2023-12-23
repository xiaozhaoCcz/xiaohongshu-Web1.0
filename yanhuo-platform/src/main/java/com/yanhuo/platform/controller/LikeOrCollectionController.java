package com.yanhuo.platform.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.LikeOrCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaozhao
 */
@RequestMapping("/likeOrCollection")
@RestController
public class LikeOrCollectionController {

    @Autowired
    LikeOrCollectionService likeOrCollectionService;

    @RequestMapping("/test")
    public Result<?> test(){
        return Result.ok();
    }


}
