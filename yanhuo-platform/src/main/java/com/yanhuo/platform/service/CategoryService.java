package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.entity.Category;
import com.yanhuo.xo.vo.CategoryVo;
import com.yanhuo.xo.vo.NoteVo;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<CategoryVo> getTreeData();

    Page<NoteVo> getNotePageByCategoryId(long currentPage, long pageSize, String categoryId, Integer type);
}
