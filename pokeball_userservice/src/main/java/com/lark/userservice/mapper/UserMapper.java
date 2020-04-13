package com.lark.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lark.usercommon.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-04-08
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    public User selectOneUser(String mobile);
}
