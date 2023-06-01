package com.yang.blog.email.controller;

import com.yang.blog.email.service.EmailService;
import com.yang.blog.pojo.entity.EmailEntity;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: Yang
 * @create: 2023-04-26
 * @Description:
 */
@RestController
@RequestMapping("/front/email")
public class EmailController {

    @Resource
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailEntity entity){
        emailService.sendEmail(entity);
        return "验证码发送成功";
    }
}
