package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.platform.service.LikeOrCollectionService;
import com.yanhuo.xo.dto.LikeOrCollectionDTO;
import com.yanhuo.xo.vo.LikeOrCollectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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


    @RequestMapping("likeOrCollectionByDTO")
    public Result<?> likeOrCollectionByDTO(@RequestBody LikeOrCollectionDTO likeOrCollectionDTO){
       // ValidatorUtils.validateEntity(likeOrCollectionDTO, AddGroup.class);
        likeOrCollectionService.likeOrCollectionByDTO(likeOrCollectionDTO);
        return Result.ok();
    }

    @RequestMapping("isLikeOrCollection")
    public Result<?> isLikeOrCollection(@RequestBody LikeOrCollectionDTO likeOrCollectionDTO){
        boolean flag = likeOrCollectionService.isLikeOrCollection(likeOrCollectionDTO);
        return Result.ok(flag);
    }


    @RequestMapping("getNoticeLikeOrCollection/{currentPage}/{pageSize}")
    public Result<?> getNoticeLikeOrCollection(@PathVariable long currentPage, @PathVariable long pageSize){
        Page<LikeOrCollectionVo> pageInfo = likeOrCollectionService.getNoticeLikeOrCollection(currentPage, pageSize);
        return Result.ok(pageInfo);
    }
}
