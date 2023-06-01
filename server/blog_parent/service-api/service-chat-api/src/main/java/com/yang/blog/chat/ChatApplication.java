package com.yang.blog.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Yang
 * @create: 2023-05-24
 * @Description:
 */
@SpringBootApplication
@ComponentScan("com.yang.blog.*")
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
