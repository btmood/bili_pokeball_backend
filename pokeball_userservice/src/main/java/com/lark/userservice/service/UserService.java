package com.lark.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lark.usercommon.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-04-08
 */
public interface UserService extends IService<User> {

    public User getOneUser(String mobile);

}
