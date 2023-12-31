package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.AlbumDTO;
import com.yanhuo.xo.entity.Album;
import com.yanhuo.xo.vo.AlbumVo;

public interface AlbumService extends IService<Album> {
    Page<Album> getAlbumPageByUserId(long currentPage, long pageSize,String userId);

    void saveAlbumByDTO(AlbumDTO albumDTO);

    AlbumVo getAlbumById(String id);

    void deleteAlbumById(String id);

    void updateAlbumByDTO(AlbumDTO albumDTO);
}
