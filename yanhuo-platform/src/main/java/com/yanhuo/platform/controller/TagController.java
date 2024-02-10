package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.platform.service.TagService;
import com.yanhuo.xo.dto.TagDTO;
import com.yanhuo.xo.entity.Tag;
import com.yanhuo.xo.vo.NoteVo;
import com.yanhuo.xo.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @RequestMapping("getHotTagList")
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
    @RequestMapping("getPageTagByKeyword/{currentPage}/{pageSize}")
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
    @RequestMapping("getTagById")
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
    @RequestMapping("getNotePageByTagId/{currentPage}/{pageSize}")
    public Result<?> getNotePageByTagId(@PathVariable long currentPage, @PathVariable long pageSize, String tagId, Integer type) {
        Page<NoteVo> imgDetailVoList = tagService.getNotePageByTagId(currentPage, pageSize, tagId, type);
        return Result.ok(imgDetailVoList);
    }

    /**
     * 添加标签
     *
     * @param tagDTO tagDTO
     * @return success
     */
    @RequestMapping("saveTagByDTO")
    public Result<?> saveTagByDTO(@RequestBody TagDTO tagDTO) {
        ValidatorUtils.validateEntity(tagDTO, DefaultGroup.class);
        tagService.saveTagByDTO(tagDTO);
        return Result.ok();
    }
}
