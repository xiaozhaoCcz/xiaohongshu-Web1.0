package com.yanhuo.search.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.search.service.UserService;
import com.yanhuo.xo.dao.UserDao;
import com.yanhuo.xo.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
}
