package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.CategoryService;
import com.yanhuo.xo.vo.CategoryVo;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("getTreeData")
    public Result<?> getTreeData() {
        List<CategoryVo> categoryList = categoryService.getTreeData();
        return Result.ok(categoryList);
    }

    @RequestMapping("getNotePageByCategoryId/{currentPage}/{pageSize}")
    public Result<?> getNotePageByCategoryId(@PathVariable long currentPage, @PathVariable long pageSize, String categoryId, Integer type) {
        Page<NoteVo> pageInfo = categoryService.getNotePageByCategoryId(currentPage, pageSize, categoryId, type);
        return Result.ok(pageInfo);
    }
}
