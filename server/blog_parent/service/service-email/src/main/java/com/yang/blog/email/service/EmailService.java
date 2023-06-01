package com.yang.blog.email.service;

import com.yang.blog.pojo.entity.EmailEntity;

/**
 * 邮箱服务
 */
public interface EmailService {

    void sendEmail(EmailEntity entity);

}
