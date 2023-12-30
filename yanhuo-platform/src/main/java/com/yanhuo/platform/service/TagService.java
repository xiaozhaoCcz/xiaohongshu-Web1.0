package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.TagDTO;
import com.yanhuo.xo.entity.Tag;
import com.yanhuo.xo.vo.NoteVo;
import com.yanhuo.xo.vo.TagVo;

import java.util.List;

public interface TagService extends IService<Tag> {
    List<TagVo> getHotTagList();

    Page<NoteVo> getNotePageByTagId(long currentPage, long pageSize, String tagId, Integer type);

    void saveTagByDTO(TagDTO tagDTO);
}
