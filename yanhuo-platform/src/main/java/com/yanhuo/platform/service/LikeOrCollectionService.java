package com.yanhuo.platform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.LikeOrCollectionDTO;
import com.yanhuo.xo.entity.LikeOrCollection;

/**
 * @author xiaozhao
 */
public interface LikeOrCollectionService extends IService<LikeOrCollection> {
    void likeOrCollectionByDTO(LikeOrCollectionDTO likeOrCollectionDTO);

    boolean isLikeOrCollection(LikeOrCollectionDTO likeOrCollectionDTO);

}
