package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.entity.AlbumNoteRelation;
import com.yanhuo.xo.vo.NoteVo;

public interface AlbumNoteRelationService extends IService<AlbumNoteRelation> {
    Page<NoteVo> getNotePageByAlbumId(long currentPage, long pageSize, String albumId, Integer type);
}
