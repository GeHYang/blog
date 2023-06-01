package com.yang.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回状态枚举类
 */
@AllArgsConstructor
public enum StatusEnum {

    REQUEST_SUCCESS(20000, "请求成功"),
    LOGIN_SUCCESS(20001, "登录成功"),
    UPDATE_SUCCESS(20002, "修改成功"),

    LOGIN_FAIL(40000, "用户名或密码错误"),
    PARAM_CHECK_FAIL(40001, "参数校验失败"),
    USER_NO_EXIST(40002, "用户不存在"),
    NO_LOGIN(40003, "未登录"),
    ILLEGAL_REQUEST(40004, "非法请求"),
    LOGIN_EXPIRED(40005, "登录过期，请重新登录！"),
    FAIL(40006, "请求错误"),
    PASSWORD_NOT_EQ(40007, "密码不正确");


    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
