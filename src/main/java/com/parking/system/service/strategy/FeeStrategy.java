package com.parking.system.service.strategy;

import com.parking.system.config.FeeConfiguration;

/**
 * Strategy Pattern Interface cho việc tính phí gửi xe
 * Tuân thủ Open/Closed Principle - Dễ dàng mở rộng thêm chiến lược tính phí mới
 * mà không cần sửa code hiện tại
 */
public interface FeeStrategy {
    
    /**
     * Tính phí gửi xe dựa trên số giờ và cấu hình
     */
    Double calculateFee(long totalHours, FeeConfiguration feeConfig);
}
