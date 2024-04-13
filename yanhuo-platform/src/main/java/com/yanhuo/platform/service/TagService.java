package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.entity.Tag;
import com.yanhuo.xo.vo.NoteVo;
import com.yanhuo.xo.vo.TagVo;

import java.util.List;

/**
 * @author xiaozhao
 */
public interface TagService extends IService<Tag> {
    /**
     * 获取热门标签
     *
     * @return List<TagVo>
     */
    List<TagVo> getHotTagList();

    /**
     * 根据标签id获取图片信息
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param tagId       标签id
     * @param type        类型
     * @return Page<NoteVo>
     */
    Page<NoteVo> getNotePageByTagId(long currentPage, long pageSize, String tagId, Integer type);

    /**
     * 根据关键词获取标签
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param keyword     关键词
     * @return 标签集
     */
    Page<Tag> getPageTagByKeyword(long currentPage, long pageSize, String keyword);
}
