package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.LikeOrCollectionService;
import com.yanhuo.xo.dto.LikeOrCollectionDTO;
import com.yanhuo.xo.vo.LikeOrCollectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaozhao
 */
@RequestMapping("/likeOrCollection")
@RestController
public class LikeOrCollectionController {

    @Autowired
    LikeOrCollectionService likeOrCollectionService;

    @RequestMapping("/test")
    public Result<?> test() {
        return Result.ok();
    }

    /**
     * 点赞或收藏
     *
     * @param likeOrCollectionDTO 点赞收藏实体
     * @return success
     */
    @PostMapping("likeOrCollectionByDTO")
    public Result<?> likeOrCollectionByDTO(@RequestBody LikeOrCollectionDTO likeOrCollectionDTO) {
        // ValidatorUtils.validateEntity(likeOrCollectionDTO, AddGroup.class);
        likeOrCollectionService.likeOrCollectionByDTO(likeOrCollectionDTO);
        return Result.ok();
    }

    /**
     * 是否点赞或收藏
     *
     * @param likeOrCollectionDTO 点赞收藏实体
     * @return flag
     */
    @PostMapping("isLikeOrCollection")
    public Result<?> isLikeOrCollection(@RequestBody LikeOrCollectionDTO likeOrCollectionDTO) {
        boolean flag = likeOrCollectionService.isLikeOrCollection(likeOrCollectionDTO);
        return Result.ok(flag);
    }

    /**
     * 得到当前用户最新的点赞和收藏信息
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @return LikeOrCollectionVo
     */
    @GetMapping("getNoticeLikeOrCollection/{currentPage}/{pageSize}")
    public Result<?> getNoticeLikeOrCollection(@PathVariable long currentPage, @PathVariable long pageSize) {
        Page<LikeOrCollectionVo> pageInfo = likeOrCollectionService.getNoticeLikeOrCollection(currentPage, pageSize);
        return Result.ok(pageInfo);
    }
}
