package com.yanhuo.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.service.CategoryService;
import com.yanhuo.xo.dao.CategoryDao;
import com.yanhuo.xo.entity.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
}
