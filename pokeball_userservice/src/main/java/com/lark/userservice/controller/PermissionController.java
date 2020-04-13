package com.lark.userservice.controller;


import com.lark.usercommon.entity.AjaxResponse;
import com.lark.usercommon.pojo.Permission;
import com.lark.userservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-03-07
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/test")
    public AjaxResponse test(){
        List<Permission> list = permissionService.list();
        if (list.size() == 0){
            Permission permission = new Permission();
            permission.setName("测试").setDescription("测试专用");
            list.add(permission);
        }
        return AjaxResponse.success(list);

    }

}

