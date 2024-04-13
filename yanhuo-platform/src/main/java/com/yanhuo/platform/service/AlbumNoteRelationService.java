package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.entity.AlbumNoteRelation;
import com.yanhuo.xo.vo.NoteSearchVo;

/**
 * @author xiaozhao
 */
public interface AlbumNoteRelationService extends IService<AlbumNoteRelation> {
    /**
     * 得到当前专辑下的所有笔记
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param albumId     专辑id
     * @param userId      用户id
     * @return 查询的笔记
     */
    Page<NoteSearchVo> getNotePageByAlbumId(long currentPage, long pageSize, String albumId, String userId);
}
