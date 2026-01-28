package com.parking.system.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security Configuration
 * Tuân thủ Single Responsibility: chỉ cấu hình security
 * Sử dụng CorsConfigConstants để tránh hardcode
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(CorsConfigConstants.ALLOWED_ORIGINS));
        configuration.setAllowedMethods(Arrays.asList(CorsConfigConstants.ALLOWED_METHODS));
        configuration.setAllowedHeaders(Arrays.asList(CorsConfigConstants.ALLOWED_HEADERS));
        configuration.setAllowCredentials(CorsConfigConstants.ALLOW_CREDENTIALS);
        configuration.setExposedHeaders(Arrays.asList(CorsConfigConstants.EXPOSED_HEADERS));
        configuration.setMaxAge(CorsConfigConstants.MAX_AGE);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        // 1. Cấu hình CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        // 2. Tắt CSRF (Cross-Site Request Forgery)
        http.csrf((CsrfConfigurer<HttpSecurity> csrf) -> {
            csrf.disable();
        });

        // 3. Cấu hình phân quyền (Authorize Requests)
        http.authorizeHttpRequests((AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) -> {
            // Cho phép OPTIONS requests (preflight) không cần authentication
            auth.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll();
            // Các quy tắc bảo mật
            auth.requestMatchers("/api/auth/**").permitAll();
            auth.requestMatchers("/api/fees/**").permitAll(); // Cho phép lấy thông tin phí công khai
            // Cho phép cả ADMIN và EMPLOYEE xem các báo cáo (bao gồm doanh thu)
            auth.requestMatchers("/api/reports/**").hasAnyRole("ADMIN", "EMPLOYEE");
            auth.requestMatchers("/api/**").hasAnyRole("ADMIN", "EMPLOYEE");
            auth.requestMatchers("/**").permitAll(); // Cho phép tất cả để frontend có thể truy cập
        });

        // 4. Cấu hình HTTP Basic dùng cấu hình mặc định
        http.httpBasic((HttpBasicConfigurer<HttpSecurity> basic) -> {
        });

        return http.build();
    }
}
