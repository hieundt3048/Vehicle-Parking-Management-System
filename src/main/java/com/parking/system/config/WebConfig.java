package com.parking.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
<<<<<<< HEAD
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:5174", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Authorization", "Content-Type")
                .maxAge(3600);
=======
    /**
     * Cấu hình CORS (Cross-Origin Resource Sharing) mappings cho toàn bộ ứng dụng
     * Method này được Spring MVC tự động gọi khi khởi động
     * 
     * @param registry CorsRegistry để đăng ký các CORS rules
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Áp dụng cho mọi API path
                .allowedOrigins("http://localhost:5173", "http://localhost:5174", "http://localhost:3000")  // Frontend origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  // HTTP methods
                .allowedHeaders("*")  // Chấp nhận mọi header (Content-Type, Authorization, etc.)
                .allowCredentials(true)  // Cho phép gửi credentials (cookies, auth tokens)
                .exposedHeaders("Authorization", "Content-Type")  // Headers mà JS có thể đọc
                .maxAge(3600);  // Cache preflight trong 1 giờ (3600 giây)
>>>>>>> master
    }
}

