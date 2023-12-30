package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.TagService;
import com.yanhuo.xo.dao.TagDao;
import com.yanhuo.xo.dto.TagDTO;
import com.yanhuo.xo.entity.Tag;
import com.yanhuo.xo.vo.NoteVo;
import com.yanhuo.xo.vo.TagVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaozhao
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {
    @Override
    public List<TagVo> getHotTagList() {
        return null;
    }

    @Override
    public Page<NoteVo> getNotePageByTagId(long currentPage, long pageSize, String tagId, Integer type) {
        return null;
    }

    @Override
    public void saveTagByDTO(TagDTO tagDTO) {

    }
}
