package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.AlbumDTO;
import com.yanhuo.xo.entity.Album;
import com.yanhuo.xo.vo.AlbumVo;

/**
 * @author xiaozhao
 */
public interface AlbumService extends IService<Album> {
    /**
     * 根据用户id获取专辑
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param userId      用户id
     * @return 专辑数
     */
    Page<Album> getAlbumPageByUserId(long currentPage, long pageSize, String userId);

    /**
     * 保存专辑
     *
     * @param albumDTO 专辑实体
     */
    void saveAlbumByDTO(AlbumDTO albumDTO);


    /**
     * 根据专辑id获取专辑
     *
     * @param albumId 专辑id
     * @return 专辑实体
     */
    AlbumVo getAlbumById(String albumId);

    /**
     * 根据专辑id删除专辑
     *
     * @param albumId 专辑id
     * @return success
     */
    void deleteAlbumById(String albumId);

    /**
     * 更新专辑
     *
     * @param albumDTO 专辑实体
     */
    void updateAlbumByDTO(AlbumDTO albumDTO);
}
