package com.yang.blog.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Yang
 * @create: 2023-04-26
 * @Description:
 */
@SpringBootApplication
@ComponentScan("com.yang.blog.*")
public class EmailApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class, args);
    }
}
