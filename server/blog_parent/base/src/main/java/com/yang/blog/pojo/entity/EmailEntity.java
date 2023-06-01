package com.yang.blog.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Yang
 * @create: 2023-04-26
 * @Description: 邮件实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailEntity implements Serializable {

    private String text; // 需要发送到文本
    private String subject; // 主题
    private String to; // 对方邮箱
    private String from; // 发送方邮箱

    public EmailEntity(String text, String subject, String to) {
        this.text = text;
        this.subject = subject;
        this.to = to;
    }
}
