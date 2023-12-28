package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.FollowerService;
import com.yanhuo.xo.dao.FollowerDao;
import com.yanhuo.xo.entity.Follower;
import org.springframework.stereotype.Service;

@Service
public class FollowerServiceImpl extends ServiceImpl<FollowerDao, Follower> implements FollowerService {
}
