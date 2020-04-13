package com.lark.usercommon.entity;

import com.lark.usercommon.pojo.Permission;
import com.lark.usercommon.pojo.Role;
import com.lark.usercommon.pojo.User;
import lombok.Data;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用户登录成功以后返回的数据
 * 基础的两个接口用于把这个类作为安全数据存到redis中
 */
@Data
public class ProfileResult implements Serializable, AuthCachePrincipal {
    
    private String mobile;
    private String username;
    private Long level;
    private String avatar;
    private Integer isVip;
    private String info;
    private Map<String, Object> roles = new HashMap<>();

    public ProfileResult() {
    }

    public ProfileResult(User user){
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.level = user.getLevel();
        this.avatar = user.getAvatar();
        this.isVip = user.getIsVip();
        this.info = user.getInfo();
        System.out.println("查出来的用户信息是" + user);

        Set<Role> roles = user.getRoles();

        Set<String> menus = new HashSet<>();
        Set<String> elements = new HashSet<>();
        Set<String> apis = new HashSet<>();

        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                String code = permission.getCode();
                if(permission.getType() == 1){
                    menus.add(code);
                }else if (permission.getType() == 2){
                    elements.add(code);
                }else {
                    apis.add(code);
                }
            }
        }

        this.roles.put("menus", menus);
        this.roles.put("elements", elements);
        this.roles.put("apis", apis);
    }
        
    

    @Override
    public String getAuthCacheKey() {
        return null;
    }
}
