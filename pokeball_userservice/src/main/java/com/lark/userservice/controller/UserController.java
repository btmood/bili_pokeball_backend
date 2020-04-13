package com.lark.userservice.controller;


import com.lark.usercommon.entity.AjaxResponse;
import com.lark.usercommon.entity.ProfileResult;
import com.lark.usercommon.exception.CustomException;
import com.lark.usercommon.exception.CustomExceptionType;
import com.lark.usercommon.pojo.Role;
import com.lark.usercommon.pojo.User;
import com.lark.userservice.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-03-07
 */
@RestController
@RequestMapping("/userservice/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("test")
    public AjaxResponse test(){
        return AjaxResponse.success("请求成功");
    }

    /**
     * 后台管理界面用户登录
     * @param loginMap
     * @return
     */
    //{"code":20000,"data":{"token":"admin-token"}}
    @PostMapping("/login")
    public AjaxResponse login(@RequestBody Map<String, String> loginMap){
        String mobile = loginMap.get("username");
        String password = loginMap.get("password");

        try {
            //1. 构造登录令牌
//        password = new Md5Hash(password, mobile, 3).toString();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(mobile, password);
            //2. 获取subject
            Subject subject = SecurityUtils.getSubject();
            //3. 调用subject的登录方法
            subject.login(usernamePasswordToken);
            //4. 获取sessionId
            String sessionId = (String) subject.getSession().getId();
            //5. 构造返回结果
            Map<String, String> map = new HashMap<>();
            map.put("token", sessionId);
            return AjaxResponse.success(map);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return AjaxResponse.error(new CustomException(CustomExceptionType.MOBILEORPASSWORDERROR, "用户名密码错误"));
        }
    }

    /**
     * 后台管理界面，登录以后获取用户信息
     * @param request
     * @return
     */
    //{"code":20000,"data":{"roles":["admin"],"introduction":"I am a super administrator","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif","name":"Super Admin"}}
    //登录以后获取用户信息
    @GetMapping("info")
    @RequiresRoles("[aaa]")
    public AjaxResponse info(HttpServletRequest request){

        //取出安全数据
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPrincipals();
//        ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
        Object primaryPrincipal = principals.getPrimaryPrincipal();
        ProfileResult result = new ProfileResult();
        BeanUtils.copyProperties(primaryPrincipal, result);
        //利用mobile查询出用户所有信息
        String mobile = result.getMobile();
        User user = userService.getOneUser(mobile);
        Set<Role> roles = user.getRoles();
        Iterator<Role> iterator = roles.iterator();

        //构建返回对象
        //role可能有多个，名字拼接一下
        String roleNameReturn = "[";
        while (iterator.hasNext()){
            Role role = iterator.next();
            String rolename = role.getRolename();
            roleNameReturn = roleNameReturn + rolename + ",";
        }
        roleNameReturn = roleNameReturn.substring(0, roleNameReturn.length()-1) + "]";
        String introduction = user.getInfo();
        String avatar = user.getAvatar();
        String name = user.getUsername();

        Map<String, Object> map = new HashMap<>();
        map.put("roles", roleNameReturn);
        map.put("introduction", introduction);
        map.put("avatar", avatar);
        map.put("name", name);

        return AjaxResponse.success(map);
    }

    @RequiresRoles(value = {"1"})
    @GetMapping("/test111")
    public AjaxResponse test111(){
        return AjaxResponse.success("测试成不成功");
    }

}

