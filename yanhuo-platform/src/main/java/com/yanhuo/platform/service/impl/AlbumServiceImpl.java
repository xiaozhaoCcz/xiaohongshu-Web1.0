package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.AlbumService;
import com.yanhuo.xo.dao.AlbumDao;
import com.yanhuo.xo.entity.Album;
import org.springframework.stereotype.Service;

@Service
public class  AlbumServiceImpl extends ServiceImpl<AlbumDao, Album> implements AlbumService {
}
