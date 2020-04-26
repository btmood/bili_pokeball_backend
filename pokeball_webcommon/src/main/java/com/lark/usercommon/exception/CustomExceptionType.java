package com.lark.usercommon.exception;

public enum CustomExceptionType {

    FAIL(10000,"操作失败"),
    UNAUTHENTICATED(10001,"您还未登录"),
    UNAUTHORISE(10002, "权限不足"),
    OTHER_ERROR(10003,"未知异常"),
    SERVER_ERROR(99999,"抱歉，系统繁忙，请稍后重试"),

    //用户操作2xxxx
    MOBILEORPASSWORDERROR(20001, "用户名或密码错误"),
    REQUESTARGUMENTSERROR(20002, "请求参数错误");
    //权限操作3xxxx
    //其它操作4xxxx

    CustomExceptionType(int type,String typeDesc){
        this.code = code;
        this.typeDesc = typeDesc;
    }

    private String typeDesc;

    private int code;

    public String getTypeDesc() {
        return typeDesc;
    }

    public int getCode() {
        return code;
    }
}
