package com.yang.blog.dynamic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Yang
 * @create: 2023-04-23
 * @Description:
 */
@SpringBootApplication
@ComponentScan("com.yang.blog.*")
public class DynamicApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicApplication.class, args);
    }
}
