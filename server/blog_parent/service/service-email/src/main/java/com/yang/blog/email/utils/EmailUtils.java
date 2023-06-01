package com.yang.blog.email.utils;

import com.yang.blog.pojo.entity.EmailEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author: Yang
 * @create: 2023-04-26
 * @Description: 发送邮件工具类
 */
@Component
public class EmailUtils {

    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;

    private static JavaMailSender mailSender;
    private static String from;

    @PostConstruct
    private void init(){
        mailSender = javaMailSender;
        from = username;
    }

    public static void sendMail(EmailEntity entity){
        // 1、构建邮件信息
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("yangBlog<" + (entity.getFrom() == null ? from : entity.getFrom()) + ">");
            helper.setSubject(entity.getSubject());
            helper.setTo(entity.getTo());
            helper.setText(entity.getText(), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
