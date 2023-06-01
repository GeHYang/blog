package com.yang.blog.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Yang
 * @create: 2023-05-16
 * @Description:
 */
@SpringBootApplication
@ComponentScan("com.yang.blog.*")
public class CommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentApplication.class, args);
    }
}
