package com.yanhuo.platform.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.myVaildator.noLogin.NoLoginIntercept;
import com.yanhuo.platform.service.CategoryService;
import com.yanhuo.xo.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xiaozhao
 */
@RequestMapping("/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取树形分类数据
     *
     * @return 分类数据
     */
    @GetMapping("getCategoryTreeData")
    @NoLoginIntercept
    public Result<?> getCategoryTreeData() {
        List<CategoryVo> categoryList = categoryService.getCategoryTreeData();
        return Result.ok(categoryList);
    }
}
