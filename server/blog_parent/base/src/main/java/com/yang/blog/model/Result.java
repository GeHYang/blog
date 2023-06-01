package com.yang.blog.model;

import com.yang.blog.pojo.vo.DynamicVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Yang
 * @create: 2023-04-23
 * @Description: 统一响应实体类
 */
@Data
public class Result<T> implements Serializable {

    private Boolean flag; // 是否请求成功
    private Integer code; // 请求状态码
    private String msg; // 响应文本
    private T data; // 响应数据

    public static Result ok(Boolean flag, Integer code, String msg){
        Result<Object> result = new Result<>();
        result.setFlag(flag);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    public static Result ok(Integer code, String msg){
        Result<Object> result = new Result<>();
        result.setFlag(true);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    public static <T>Result<T> ok(Integer code, String msg, T data){
        Result<T> result = new Result<>();
        result.setFlag(true);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> ok(Boolean flag, Integer code, String msg, T data){
        Result<T> result = new Result<>();
        result.setFlag(flag);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> ok(Boolean flag, Integer code, T data){
        Result<T> result = new Result<>();
        result.setFlag(flag);
        result.setCode(code);
        result.setMsg("请求成功");
        result.setData(data);
        return result;
    }

    public static Result error(){
        return Result.ok(false, CodeConstant.ERROR, "失败");
    }

    public static Result error(StatusEnum statusEnum){
        return Result.error(statusEnum.getCode(), statusEnum.getMsg());
    }

    public static Result error(Integer code, String msg){
        return Result.ok(false, code, msg);
    }

    public static <T>Result<T> ok(StatusEnum statusEnum, T data) {
        return Result.ok(statusEnum.getCode(), statusEnum.getMsg(), data);
    }

    public static Result<String> ok(StatusEnum statusEnum) {
        return Result.ok(statusEnum.getCode(), statusEnum.getMsg());
    }
}
