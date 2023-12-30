package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.CategoryService;
import com.yanhuo.xo.dao.CategoryDao;
import com.yanhuo.xo.entity.Category;
import com.yanhuo.xo.vo.CategoryVo;
import com.yanhuo.xo.vo.NoteVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
    @Override
    public List<CategoryVo> getTreeData() {
        return null;
    }

    @Override
    public Page<NoteVo> getNotePageByCategoryId(long currentPage, long pageSize, String categoryId, Integer type) {
        return null;
    }
}
