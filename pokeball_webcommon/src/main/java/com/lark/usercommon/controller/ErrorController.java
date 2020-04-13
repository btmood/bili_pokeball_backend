package com.lark.usercommon.controller;

import com.lark.usercommon.entity.AjaxResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * shiro公共错误处理模块
 */
@RestController
public class ErrorController {

    //公共错误跳转
    @RequestMapping("/autherror")
    public AjaxResponse autherror(int code){
        return code == 1 ? AjaxResponse.error("未登录") : AjaxResponse.error("未授权");
    }
}
