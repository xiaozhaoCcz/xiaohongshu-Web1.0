package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.common.validator.group.UpdateGroup;
import com.yanhuo.platform.service.AlbumService;
import com.yanhuo.xo.dto.AlbumDTO;
import com.yanhuo.xo.vo.AlbumVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/album")
@RestController
public class AlbumController {

    @Autowired
    AlbumService albumService;
    @RequestMapping("getAlbumPageByUserId/{currentPage}/{pageSize}")
    public Result<?> getAlbumPageByUserId(@PathVariable long currentPage, @PathVariable long pageSize,String userId){
        Page<AlbumVo> page =  albumService.getAlbumPageByUserId(currentPage,pageSize,userId);
        return Result.ok(page);
    }

    @RequestMapping("saveAlbumByDTO")
    public Result<?> saveAlbumByDTO(@RequestBody AlbumDTO albumDTO) {
        ValidatorUtils.validateEntity(albumDTO, AddGroup.class);
        albumService.saveAlbumByDTO(albumDTO);
        return Result.ok();
    }


    @RequestMapping("getAlbumById")
    public Result<?> getAlbumById(String albumId) {
        AlbumVo albumVo = albumService.getAlbumById(albumId);
        return Result.ok(albumVo);
    }


    @RequestMapping("deleteAlbumById")
    public Result<?> deleteAlbumById(String albumId, String userId) {
        albumService.deleteAlbumById(albumId, userId);
        return Result.ok();
    }

    @RequestMapping("updateAlbumByDTO")
    public Result<?> updateAlbumByDTO(@RequestBody AlbumDTO albumDTO) {
        ValidatorUtils.validateEntity(albumDTO, UpdateGroup.class);
        albumService.updateAlbumByDTO(albumDTO);
        return Result.ok();
    }
}
