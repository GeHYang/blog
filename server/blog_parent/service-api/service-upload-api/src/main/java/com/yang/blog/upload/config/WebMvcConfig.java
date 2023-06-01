package com.yang.blog.upload.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author: Yang
 * @create: 2023-04-24
 * @Description:
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 对静态资源进行映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absPath = System.getProperties().getProperty("user.dir") + "/base/src/main/resources/static/";

        registry.addResourceHandler("/static/**") // 配置静态资源访问路径
                .addResourceLocations("file:" + absPath); // 配置静态资源
    }
}
