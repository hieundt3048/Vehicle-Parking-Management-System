package com.parking.system.controller;

<<<<<<< HEAD
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
=======
import java.util.List;

>>>>>>> master
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
<<<<<<< HEAD
=======
import com.parking.system.service.TokenService;
>>>>>>> master
import com.parking.system.service.UserService;

import jakarta.validation.Valid;

<<<<<<< HEAD
=======
/**
 * Controller xử lý authentication và user management
 * Tuân thủ Single Responsibility: chỉ xử lý HTTP requests/responses
 * Business logic được delegate cho Service layer
 */
>>>>>>> master
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class AuthController {
    
<<<<<<< HEAD
    @Autowired
    private UserService userService;
    
    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Validate dữ liệu đầu vào
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Tên đăng nhập không được để trống"));
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Mật khẩu không được để trống"));
            }
            
            // Xác thực user
            User user = userService.authenticate(request.getUsername(), request.getPassword());
            
            // Tạo token đơn giản (Base64 encode của username:password)
            // Trong production, nên dùng JWT token
            String token = Base64.getEncoder().encodeToString(
                (request.getUsername() + ":" + request.getPassword()).getBytes()
            );
            
            // Tạo response (ẩn password)
            User userResponse = new User();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setFullName(user.getFullName());
            userResponse.setRole(user.getRole());
            userResponse.setActive(user.getActive());
            // Không set password
            
            LoginResponse loginResponse = new LoginResponse(token, userResponse, "Đăng nhập thành công");
            
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để debug
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Tên đăng nhập hoặc mật khẩu không đúng"));
        }
    }
    
    // Đăng ký tài khoản (Admin/Employee)
    // Username, Password, FullName, Role
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody @Valid RegisterRequest request) {
        try {
            // Nếu không có role hoặc role == null, mặc định là EMPLOYEE
            User.Role userRole = (request.getRole() != null) ? request.getRole() : User.Role.EMPLOYEE;
            
            User newUser = userService.createUser(
                request.getUsername(), 
                request.getPassword(), 
                request.getFullName(), 
                userRole
            );
            return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công", newUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi đăng ký: " + e.getMessage()));
        }
    }
    
    // Xem danh sách nhân viên (Dành cho Admin check)
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thành công", users));
=======
    private final UserService userService;
    private final TokenService tokenService;
    
    // Constructor injection - tốt hơn field injection, dễ test hơn
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
     * 
     * @Valid annotation tự động validate theo constraints trong RegisterRequest
     * Exception được handle bởi GlobalExceptionHandler
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
     * Chỉ dành cho Admin (được bảo vệ bởi SecurityConfig)
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        
        // Loại bỏ password khỏi response
        List<User> safeUsers = users.stream()
            .map(tokenService::createSafeUserResponse)
            .toList();
        
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thành công", safeUsers));
>>>>>>> master
    }
}