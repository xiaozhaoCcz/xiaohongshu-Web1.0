package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.TagService;
import com.yanhuo.xo.dao.TagDao;
import com.yanhuo.xo.entity.Tag;
import org.springframework.stereotype.Service;

/**
 * @author xiaozhao
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {
}
