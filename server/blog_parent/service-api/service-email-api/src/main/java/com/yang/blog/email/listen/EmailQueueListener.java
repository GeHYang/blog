package com.yang.blog.email.listen;

import com.yang.blog.email.service.EmailService;
import com.yang.blog.pojo.entity.EmailEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: Yang
 * @create: 2023-05-24
 * @Description: 邮件队列监听
 */
@Component
@Slf4j
public class EmailQueueListener {

    @Resource
    private EmailService emailService;

    @RabbitListener(queues = "email.code")
    public void emailCodeListener(EmailEntity entity){
        emailService.sendEmail(entity);
    }

}
