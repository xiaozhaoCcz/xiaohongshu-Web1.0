package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.TagService;
import com.yanhuo.xo.entity.Tag;
import com.yanhuo.xo.vo.NoteVo;
import com.yanhuo.xo.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiaozhao
 */
@RequestMapping("/tag")
@RestController
public class TagController {

    @Autowired
    TagService tagService;

    /**
     * 获取热门标签
     *
     * @return List<TagVo>
     */
    @GetMapping("getHotTagList")
    public Result<?> getHotTagList() {
        List<TagVo> voList = tagService.getHotTagList();
        return Result.ok(voList);
    }

    /**
     * 根据关键词获取标签
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param keyword     关键词
     * @return 标签集
     */
    @GetMapping("getPageTagByKeyword/{currentPage}/{pageSize}")
    public Result<?> getPageTagByKeyword(@PathVariable long currentPage, @PathVariable long pageSize, String keyword) {
        Page<Tag> page = tagService.getPageTagByKeyword(currentPage, pageSize, keyword);
        return Result.ok(page);
    }

    /**
     * 得到当前标签信息
     *
     * @param tagId
     * @return tag
     */
    @GetMapping("getTagById")
    public Result<?> getTagById(String tagId) {
        Tag tag = tagService.getById(tagId);
        return Result.ok(tag);
    }

    /**
     * 根据标签id获取图片信息
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param tagId       标签id
     * @param type        类型
     * @return Page<NoteVo>
     */
    @GetMapping("getNotePageByTagId/{currentPage}/{pageSize}")
    public Result<?> getNotePageByTagId(@PathVariable long currentPage, @PathVariable long pageSize, String tagId, Integer type) {
        Page<NoteVo> imgDetailVoList = tagService.getNotePageByTagId(currentPage, pageSize, tagId, type);
        return Result.ok(imgDetailVoList);
    }
}
