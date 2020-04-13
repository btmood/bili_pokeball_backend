package com.lark.usercommon.exception;

import com.lark.usercommon.entity.AjaxResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public AjaxResponse customerException(CustomException e){

        //TODO:400需要告知用户
        //TODO:500需要持久化

        return AjaxResponse.error(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AjaxResponse exception(Exception e){
        //TODO 将异常信息持久化处理

        //没有被程序员发现，并转换为CustomException的异常，都是其他异常或者未知异常.
        return AjaxResponse.error(new CustomException(CustomExceptionType.OTHER_ERROR,"未知异常"));
    }

}