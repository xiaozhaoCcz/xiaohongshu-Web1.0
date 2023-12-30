package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.FollowerVo;

/**
 * @author xiaozhao
 */
public interface UserService extends IService<User> {
    Page<TrendVo> getTrendPageByUser(long currentPage, long pageSize, Integer type);

    User updateUser(User user);

    Page<FollowerVo> getUserPageByKeyword(long currentPage, long pageSize, String keyword);
}
