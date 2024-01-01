package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.service.LikeOrCollectionService;
import com.yanhuo.xo.dao.LikeOrCollectionDao;
import com.yanhuo.xo.dto.LikeOrCollectionDTO;
import com.yanhuo.xo.entity.LikeOrCollection;
import org.springframework.stereotype.Service;

/**
 * @author xiaozhao
 */
@Service
public class LikeOrCollectionServiceImpl extends ServiceImpl<LikeOrCollectionDao, LikeOrCollection> implements LikeOrCollectionService {
    @Override
    public void likeByDTO(LikeOrCollectionDTO likeOrCollectionDTO) {

        // 点赞评论或者笔记
        LikeOrCollection likeOrCollection = ConvertUtils.sourceToTarget(likeOrCollectionDTO, LikeOrCollection.class);
        likeOrCollection.setTimestamp(System.currentTimeMillis());
        this.save(likeOrCollection);
    }
}
