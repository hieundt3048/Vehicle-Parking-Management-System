package com.parking.system.service.strategy;

import org.springframework.stereotype.Component;

import com.parking.system.config.FeeConfiguration;

/**
 * Chiến lược tính phí cho ô tô
 */
@Component
public class CarFeeStrategy implements FeeStrategy {
    
    @Override
    public Double calculateFee(long totalHours, FeeConfiguration feeConfig) {
        // Nếu trong block đầu tiên (mặc định 2 giờ) -> tính giá block đầu
        if (totalHours <= feeConfig.getFirstBlockHours()) {
            return feeConfig.getCarFirstBlock();
        }
        
        // Nếu vượt block đầu tiên -> tính giá block đầu + các giờ thêm
        long extraHours = totalHours - feeConfig.getFirstBlockHours();
        return feeConfig.getCarFirstBlock() + (extraHours * feeConfig.getCarNextHour());
    }
}
