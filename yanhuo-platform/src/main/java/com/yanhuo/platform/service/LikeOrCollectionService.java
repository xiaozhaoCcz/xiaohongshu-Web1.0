package com.yanhuo.platform.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.LikeOrCollectionDTO;
import com.yanhuo.xo.entity.LikeOrCollection;
import com.yanhuo.xo.vo.LikeOrCollectionVo;

/**
 * @author xiaozhao
 */
public interface LikeOrCollectionService extends IService<LikeOrCollection> {
    void likeOrCollectionByDTO(LikeOrCollectionDTO likeOrCollectionDTO);

    boolean isLikeOrCollection(LikeOrCollectionDTO likeOrCollectionDTO);

    Page<LikeOrCollectionVo> getNoticeLikeOrCollection(long currentPage, long pageSize);
}
