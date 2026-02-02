package com.parking.system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parking.system.dto.ApiResponse;
import com.parking.system.dto.LoginRequest;
import com.parking.system.dto.LoginResponse;
import com.parking.system.dto.RegisterRequest;
import com.parking.system.entity.User;
import com.parking.system.service.TokenService;
import com.parking.system.service.UserService;

import jakarta.validation.Valid;

/**
 * Controller xử lý authentication và user managemen
 * Tuân thủ Single Responsibility: chỉ xử lý HTTP requests/responses
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserService userService;
    private final TokenService tokenService;
    
    // Constructor injection
    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }
    
    /**
     * API đăng nhập
     * POST /api/auth/login
     * 
     * Exception handling được xử lý bởi GlobalExceptionHandler
     * Validation đơn giản ở đây, business logic ở UserService
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Validate input cơ bản
        validateLoginRequest(request);
        
        // Xác thực user (ném exception nếu fail)
        User user = userService.authenticate(request.getUsername(), request.getPassword());
        
        // Tạo token
        String token = tokenService.generateToken(request.getUsername(), request.getPassword());
        
        // Tạo safe user response (không có password)
        User safeUser = tokenService.createSafeUserResponse(user);
        
        // Trả về response
        LoginResponse response = new LoginResponse(token, safeUser, "Đăng nhập thành công");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Helper method: Xác thực yêu cầu đăng nhập
     */
    private void validateLoginRequest(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống");
        }
        
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
    }
    
    /**
     * API đăng ký tài khoản mới
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody @Valid RegisterRequest request) {
        // Nếu không có role, mặc định là EMPLOYEE
        User.Role userRole = (request.getRole() != null) ? request.getRole() : User.Role.EMPLOYEE;
        
        // Tạo user mới
        User newUser = userService.createUser(
            request.getUsername(), 
            request.getPassword(), 
            request.getFullName(), 
            userRole
        );
        
        // Tạo safe response (không có password)
        User safeUser = tokenService.createSafeUserResponse(newUser);
        
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công", safeUser));
    }
    
    /**
     * API lấy danh sách tất cả users
     * GET /api/auth/users
     * 
     * Chỉ dành cho Admin
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        
        // Loại bỏ password khỏi response
        List<User> safeUsers = users.stream()
            .map(tokenService::createSafeUserResponse)
            .toList();
        
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thành công", safeUsers));
    }
}