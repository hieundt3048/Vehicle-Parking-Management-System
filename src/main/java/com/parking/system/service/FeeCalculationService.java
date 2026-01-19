package com.parking.system.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.parking.system.config.FeeConfiguration;
import com.parking.system.entity.ParkingZone;

/**
 * Service tính toán phí gửi xe
 * Tuân thủ Single Responsibility: chỉ làm một việc là tính phí
 * Tuân thủ Open/Closed: dễ dàng mở rộng bằng cách thay đổi FeeConfiguration
 */
@Service
public class FeeCalculationService {
    
    private final FeeConfiguration feeConfig;
    
    // Constructor injection để có thể inject configuration
    public FeeCalculationService(FeeConfiguration feeConfig) {
        this.feeConfig = feeConfig;
    }

    /**
     * Tính phí gửi xe dựa trên thời gian và loại xe
     * 
     * @param entryTime Thời gian vào
     * @param exitTime Thời gian ra
     * @param vehicleType Loại xe (MOTORBIKE/CAR)
     * @return Số tiền phải trả
     */
    public Double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime, ParkingZone.VehicleType vehicleType) {
        // Validate dữ liệu đầu vào
        validateInput(entryTime, exitTime, vehicleType);

        // Tính thời gian gửi 
        long totalHours = calculateParkingHours(entryTime, exitTime);
        
        // Chọn công thức theo loại xe
        if (vehicleType == ParkingZone.VehicleType.MOTORBIKE) {
            return calculate(totalHours, feeConfig.getMotorbikeFirstBlock(), feeConfig.getMotorbikeNextHour());
        } else {
            return calculate(totalHours, feeConfig.getCarFirstBlock(), feeConfig.getCarNextHour());
        }
    }
    
    /**
     * Validate input parameters
     */
    private void validateInput(LocalDateTime entryTime, LocalDateTime exitTime, ParkingZone.VehicleType vehicleType) {
        if (entryTime == null || exitTime == null || vehicleType == null) {
            throw new IllegalArgumentException("Thời gian vào, thời gian ra và loại xe không được null");
        }

        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Giờ ra không thể trước giờ vào");
        }
    }
    
    /**
     * Tính số giờ gửi xe (làm tròn lên)
     */
    private long calculateParkingHours(LocalDateTime entryTime, LocalDateTime exitTime) {
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        long hours = (long) Math.ceil(minutes / 60.0);
        
        // Nếu gửi chưa đến 1 phút cũng tính tối thiểu là 1 giờ
        return hours == 0 ? 1 : hours;
    }

    /**
     * Công thức tính phí chung
     * Block đầu tiên: firstBlockPrice (mặc định 2 giờ)
     * Các giờ tiếp theo: nextHourPrice per giờ
     */
    private Double calculate(long totalHours, double firstBlockPrice, double nextHourPrice) {
        if (totalHours <= feeConfig.getFirstBlockHours()) {
            return firstBlockPrice;
        } else {
            long extraHours = totalHours - feeConfig.getFirstBlockHours();
            return firstBlockPrice + (extraHours * nextHourPrice);
        }
    }
}