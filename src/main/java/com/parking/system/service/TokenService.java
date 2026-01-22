package com.parking.system.service;

import java.util.Base64;

import org.springframework.stereotype.Service;

import com.parking.system.entity.User;

/**
 * Service chuyên xử lý token generation và validation
 * Tuân thủ Single Responsibility Principle - chỉ làm một việc
 * Dễ dàng thay thế bằng JWT token sau này mà không ảnh hưởng code khác
 */
@Service
public class TokenService {
    
    /**
     * Tạo token đơn giản cho user
     * @param username Tên đăng nhập
     * @param password Mật khẩu (raw)
     * @return Token string
     */
    public String generateToken(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username và password không được null");
        }
        
        String credentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
    
    /**
     * Xác thực token format
     * @return true nếu token hợp lệ
     */
    public boolean isValidToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        try {
            byte[] decoded = Base64.getDecoder().decode(token);
            String credentials = new String(decoded);
            return credentials.contains(":");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Tạo user response an toàn
     * @param user User entity từ database
     * @return User object đã loại bỏ sensitive data
     */
    public User createSafeUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setFullName(user.getFullName());
        safeUser.setRole(user.getRole());
        safeUser.setActive(user.getActive());
        
        return safeUser;
    }
}
