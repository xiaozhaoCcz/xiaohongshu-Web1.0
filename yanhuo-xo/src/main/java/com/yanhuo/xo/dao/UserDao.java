package com.yanhuo.xo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhuo.xo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaozhao
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
}
