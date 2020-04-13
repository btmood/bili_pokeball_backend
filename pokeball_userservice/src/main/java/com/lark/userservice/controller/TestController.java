package com.lark.userservice.controller;

import com.lark.usercommon.entity.AjaxResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test01")
    public AjaxResponse test01(){
        return AjaxResponse.success();
    }

    @GetMapping("/login")
    public AjaxResponse login(){
        return AjaxResponse.success("哎呦，不错哦！");
    }
}
