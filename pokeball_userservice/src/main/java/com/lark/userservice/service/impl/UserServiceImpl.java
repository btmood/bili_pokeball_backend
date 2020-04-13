package com.lark.userservice.service.impl;

import com.lark.usercommon.pojo.User;
import com.lark.userservice.mapper.UserMapper;
import com.lark.userservice.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-04-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getOneUser(String mobile) {
        User user = userMapper.selectOneUser(mobile);
        return user;
    }
}
