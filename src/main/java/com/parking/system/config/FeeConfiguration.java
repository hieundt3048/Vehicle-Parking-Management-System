package com.parking.system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Configuration class cho parking fee
 * Tuân thủ Open/Closed Principle - Dễ dàng thay đổi giá trong application.properties mà không cần sửa code
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "parking.fee")
public class FeeConfiguration {
    
    // Cấu hình xe máy
    private double motorbikeFirstBlock = 5000.0;  // 2h đầu
    private double motorbikeNextHour = 2000.0;     // Các giờ sau
    
    // Cấu hình ô tô
    private double carFirstBlock = 10000.0;        // 2h đầu
    private double carNextHour = 5000.0;           // Các giờ sau
    
    // Block đầu tiên tính bao nhiêu giờ
    private long firstBlockHours = 2;
    
    // Getters and Setters
    public double getMotorbikeFirstBlock() {
        return motorbikeFirstBlock;
    }
    
    public void setMotorbikeFirstBlock(double motorbikeFirstBlock) {
        this.motorbikeFirstBlock = motorbikeFirstBlock;
    }
    
    public double getMotorbikeNextHour() {
        return motorbikeNextHour;
    }
    
    public void setMotorbikeNextHour(double motorbikeNextHour) {
        this.motorbikeNextHour = motorbikeNextHour;
    }
    
    public double getCarFirstBlock() {
        return carFirstBlock;
    }
    
    public void setCarFirstBlock(double carFirstBlock) {
        this.carFirstBlock = carFirstBlock;
    }
    
    public double getCarNextHour() {
        return carNextHour;
    }
    
    public void setCarNextHour(double carNextHour) {
        this.carNextHour = carNextHour;
    }
    
    public long getFirstBlockHours() {
        return firstBlockHours;
    }
    
    public void setFirstBlockHours(long firstBlockHours) {
        this.firstBlockHours = firstBlockHours;
    }
}
