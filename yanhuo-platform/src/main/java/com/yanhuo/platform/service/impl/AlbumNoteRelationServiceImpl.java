package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.AlbumNoteRelationService;
import com.yanhuo.xo.dao.AlbumNoteRelationDao;
import com.yanhuo.xo.entity.AlbumNoteRelation;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.stereotype.Service;

@Service
public class AlbumNoteRelationServiceImpl extends ServiceImpl<AlbumNoteRelationDao, AlbumNoteRelation> implements AlbumNoteRelationService {
    @Override
    public Page<NoteVo> getNotePageByAlbumId(long currentPage, long pageSize, String albumId, Integer type) {
        return null;
    }
}
