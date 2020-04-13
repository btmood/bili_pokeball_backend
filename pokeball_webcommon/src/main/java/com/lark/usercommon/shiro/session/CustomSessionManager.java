package com.lark.usercommon.shiro.session;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义的sessionManager
 *
 * 1. 用户发出请求，从请求头里面获取sessionID
 */
public class CustomSessionManager extends DefaultWebSessionManager {

    /**
     * 指定sessionId的获取方式
     *
     * 头信息中具有sessionId
     *      请求头： Authorization ： sessionId
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //获取请求头中的数据
        String id = WebUtils.toHttp(request).getHeader("Authorization");
        if(StringUtils.isEmpty(id)){
            //如果第一次访问没有sessionId，生成sessionId
            //我们这里是重写方法，父类里面这个方法能自己生成sessionId，所以直接调用父类方法就行了
            return super.getSessionId(request, response);
        }else{
            //返回sessionId

            //请求头字段格式是Bearer sessionId
            id = id.replace("Bearer " , "");
            //这个sessionId是从哪里获取到的:请求头
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
            //这个sessionId具体是什么
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            //这个sessionId需不需要验证
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }
    }
}
