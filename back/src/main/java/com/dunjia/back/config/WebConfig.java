package com.dunjia.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 所有的 API 路径都允许跨域请求
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000")  // 允许来自 Vue.js 前端（3000端口）的请求
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的 HTTP 请求方法
                .allowedHeaders("*")  // 允许所有的请求头
                .allowCredentials(true);  // 允许发送 Cookie 和认证信息
    }
}
