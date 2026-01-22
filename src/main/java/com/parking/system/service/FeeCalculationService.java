package com.parking.system.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.parking.system.config.FeeConfiguration;
import com.parking.system.entity.ParkingZone;
import com.parking.system.service.strategy.FeeStrategy;
import com.parking.system.service.strategy.FeeStrategyFactory;

/**
 * Service tính toán phí gửi xe
 * Tuân thủ Single Responsibility: chỉ làm một việc là tính phí
 * Tuân thủ Open/Closed: dễ dàng mở rộng bằng cách thêm Strategy mới
 * Sử dụng Strategy Pattern để tính phí linh hoạt theo loại xe
 */
@Service
public class FeeCalculationService {
    
    private final FeeConfiguration feeConfig;
    private final FeeStrategyFactory strategyFactory;
    
    // Constructor injection để inject configuration và strategy factory
    public FeeCalculationService(FeeConfiguration feeConfig, FeeStrategyFactory strategyFactory) {
        this.feeConfig = feeConfig;
        this.strategyFactory = strategyFactory;
    }

    /**
     * Tính phí gửi xe dựa trên thời gian và loại xe
     * Sử dụng Strategy Pattern để chọn chiến lược tính phí phù hợp
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
        
        // Lấy strategy phù hợp với loại xe và tính phí
        FeeStrategy strategy = strategyFactory.getStrategy(vehicleType);
        return strategy.calculateFee(totalHours, feeConfig);
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
<<<<<<< HEAD
=======

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
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
}