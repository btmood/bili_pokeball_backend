package com.lark.usercommon.entity;

import com.lark.usercommon.exception.CustomException;
import com.lark.usercommon.exception.CustomExceptionType;
import lombok.Data;

@Data
public class AjaxResponse {
    private boolean success;
    private int code;
    private String message;
    private Object data;

    private AjaxResponse(){}

    public static AjaxResponse error(CustomException e){
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setSuccess(false);
        resultBean.setCode(e.getCode());
        //错误信息配置
        if(e.getCode() == CustomExceptionType.FAIL.getCode()){
            resultBean.setMessage(e.getMessage());
        }else if(e.getCode() == CustomExceptionType.UNAUTHENTICATED.getCode()){
            resultBean.setMessage(e.getMessage());
        }else if(e.getCode() == CustomExceptionType.UNAUTHORISE.getCode()){
            resultBean.setMessage(e.getMessage());
        }else if(e.getCode() == CustomExceptionType.SERVER_ERROR.getCode()){
            resultBean.setMessage(e.getMessage());
        }else if(e.getCode() == CustomExceptionType.MOBILEORPASSWORDERROR.getCode()){
            resultBean.setMessage(e.getMessage());
        }else {
            resultBean.setMessage("出现未知异常");
        }
        return resultBean;
    }

    public static AjaxResponse success(){
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setSuccess(true);
        resultBean.setCode(20000);
        resultBean.setMessage("success");
        return resultBean;
    }

    public static AjaxResponse success(Object data){
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setSuccess(true);
        resultBean.setCode(20000);
        resultBean.setMessage("success");
        resultBean.setData(data);
        return resultBean;
    }

    public static AjaxResponse error(Object data){
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setSuccess(false);
        resultBean.setCode(20001);
        resultBean.setMessage("操作失败");
        resultBean.setData(data);
        return resultBean;
    }

    public static AjaxResponse error(String msg){
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setSuccess(false);
        resultBean.setCode(CustomExceptionType.FAIL.getCode());
        resultBean.setMessage(msg);
        return resultBean;
    }


}
