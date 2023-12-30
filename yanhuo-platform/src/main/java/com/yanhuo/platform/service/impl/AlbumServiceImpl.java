package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.AlbumService;
import com.yanhuo.xo.dao.AlbumDao;
import com.yanhuo.xo.dto.AlbumDTO;
import com.yanhuo.xo.entity.Album;
import com.yanhuo.xo.vo.AlbumVo;
import org.springframework.stereotype.Service;

@Service
public class  AlbumServiceImpl extends ServiceImpl<AlbumDao, Album> implements AlbumService {
    @Override
    public Page<AlbumVo> getAlbumPageByUserId(long currentPage, long pageSize,String userId) {
        return null;
    }

    @Override
    public void saveAlbumByDTO(AlbumDTO albumDTO) {

    }

    @Override
    public AlbumVo getAlbumById(String id) {
        return null;
    }


    @Override
    public void deleteAlbumById(String id, String userId) {

    }


    @Override
    public void updateAlbumByDTO(AlbumDTO albumDTO) {

    }

}
