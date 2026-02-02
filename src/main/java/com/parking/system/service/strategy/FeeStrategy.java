package com.parking.system.service.strategy;

import com.parking.system.config.FeeConfiguration;

/**
 * Strategy Pattern Interface cho việc tính phí gửi xe
 */
public interface FeeStrategy {
    
    /**
     * Tính phí gửi xe dựa trên số giờ và cấu hình
     */
    Double calculateFee(long totalHours, FeeConfiguration feeConfig);
}
