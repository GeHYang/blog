package com.yang.blog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: Yang
 * @create: 2023-05-24
 * @Description: WebSocket统一返回实体
 */
@Data
public class WSResult<T> {

    private String type; // 消息类型
    private T data; // 响应数据

    public WSResult(Types types, T data) {
        this.type = types.msg;
        this.data = data;
    }

    @AllArgsConstructor
    public enum Types {
        chat("chat"),
        sys("sys");
        private String msg;

    }
}
