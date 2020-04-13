package com.lark.userservice.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lark.usercommon.entity.ProfileResult;
import com.lark.usercommon.pojo.User;
import com.lark.usercommon.shiro.realm.PokeballRealm;
import com.lark.userservice.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends PokeballRealm {

    @Autowired
    private UserService userService;

    //认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1. 取出用户名和密码
        UsernamePasswordToken upToken =  (UsernamePasswordToken) token;
        String mobile = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //2. 根据手机号查询用户
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("mobile", mobile);
//        User user = userService.getOne(wrapper);
        User user = userService.getOneUser(mobile);
        //3. 判断用户是否存在，密码是否一致
        if(user != null && password.equals(user.getPassword())){
            ProfileResult result = new ProfileResult(user);
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(result, user.getPassword(), this.getName());
            return info;
        }

        //返回null，会抛出异常，表示用户名密码不匹配
        return null;
    }
}
