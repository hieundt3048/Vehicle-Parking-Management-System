package com.parking.system.config;

/**
 * CORS Configuration Constants
 * Tập trung config CORS cho phép frontend (localhost:5173) gọi API backend (localhost:8080)
 * Tránh hardcode lặp lại trong nhiều file - Tuân thủ DRY principle
 */
public final class CorsConfigConstants {
    
    // Private constructor - Utility class không cho phép tạo instance
    private CorsConfigConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Các frontend origins được phép gọi API
     */
    public static final String[] ALLOWED_ORIGINS = {
        "http://localhost:5173",  // Vite dev server
        "http://localhost:5174",  // Vite alternative port
        "http://localhost:3000"   // Create React App
    };
    
    /** Các HTTP methods được phép sử dụng */
    public static final String[] ALLOWED_METHODS = {
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
    };
    
    /** Cho phép tất cả request headers */
    public static final String[] ALLOWED_HEADERS = {"*"};
    
    /** Response headers mà frontend được phép đọc */
    public static final String[] EXPOSED_HEADERS = {
        "Authorization", "Content-Type"
    };
    
    /** Cho phép gửi credentials (cookies, auth headers) */
    public static final boolean ALLOW_CREDENTIALS = true;
    
    /** Yêu cầu kiểm tra trước khi lưu trong 1 giờ */
    public static final long MAX_AGE = 3600L;
}
