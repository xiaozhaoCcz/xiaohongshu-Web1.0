package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.AlbumNoteRelationService;
import com.yanhuo.xo.dao.AlbumNoteRelationDao;
import com.yanhuo.xo.entity.AlbumNoteRelation;
import org.springframework.stereotype.Service;

@Service
public class AlbumNoteRelationServiceImpl extends ServiceImpl<AlbumNoteRelationDao, AlbumNoteRelation> implements AlbumNoteRelationService {
}
