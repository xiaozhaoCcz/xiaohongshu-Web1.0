package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.TreeUtils;
import com.yanhuo.platform.service.CategoryService;
import com.yanhuo.xo.dao.CategoryDao;
import com.yanhuo.xo.entity.Category;
import com.yanhuo.xo.vo.CategoryVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaozhao
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
    @Override
    public List<CategoryVo> getCategoryTreeData() {
        List<Category> list = this.list(new QueryWrapper<Category>().orderByDesc("like_count"));
        List<CategoryVo> voList = ConvertUtils.sourceToTarget(list, CategoryVo.class);
        return TreeUtils.build(voList);
    }
}
