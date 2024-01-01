package com.yanhuo.platform.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.platform.service.LikeOrCollectionService;
import com.yanhuo.xo.dto.LikeOrCollectionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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


    @RequestMapping("/likeByDTO")
    public Result<?> likeByDTO(@RequestBody LikeOrCollectionDTO likeOrCollectionDTO){
       // ValidatorUtils.validateEntity(likeOrCollectionDTO, AddGroup.class);
        likeOrCollectionService.likeByDTO(likeOrCollectionDTO);
        return Result.ok();
    }

}
