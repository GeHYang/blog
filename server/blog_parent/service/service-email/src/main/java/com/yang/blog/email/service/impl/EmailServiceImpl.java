package com.yang.blog.email.service.impl;

import com.yang.blog.email.service.EmailService;
import com.yang.blog.pojo.entity.EmailEntity;
import com.yang.blog.email.utils.EmailUtils;
import org.springframework.stereotype.Service;


/**
 * @author: Yang
 * @create: 2023-04-26
 * @Description:
 */
@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(EmailEntity entity) {
        // 1、发送邮件信息
//        EmailUtils.sendMail(entity);
        // 1.1 已多线程的方式发送邮箱信息
        new Thread(() -> EmailUtils.sendMail(entity)).start();
    }
}
