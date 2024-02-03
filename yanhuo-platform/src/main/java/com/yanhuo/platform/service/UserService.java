package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.platform.vo.TrendVo;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.FollowerVo;
import com.yanhuo.xo.vo.NoteSearchVo;

/**
 * @author xiaozhao
 */
public interface UserService extends IService<User> {
    /**
     * 得到当前用户的动态
     *
     * @param currentPage 当前页
     * @param pageSize    分页数
     * @param userId      用户id
     * @param type        类型
     * @return Page<NoteSearchVo>
     */
    Page<NoteSearchVo> getTrendPageByUser(long currentPage, long pageSize, String userId, Integer type);

    /**
     * 更新用户信息
     *
     * @param user 用户实体
     * @return user
     */
    User updateUser(User user);

    /**
     * 查找用户信息
     *
     * @param keyword 关键词
     * @return FollowerVo
     */
    Page<FollowerVo> getUserPageByKeyword(long currentPage, long pageSize, String keyword);

    /**
     * 保存用户的搜索记录
     *
     * @param keyword 关键词
     * @return success
     */
    void saveUserSearchRecord(String keyword);
}
