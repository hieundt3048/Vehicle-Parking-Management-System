package com.parking.system.service.strategy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.parking.system.entity.ParkingZone;

/**
 * Factory Pattern để chọn FeeStrategy phù hợp
 */
@Component
public class FeeStrategyFactory {
    
    private final Map<ParkingZone.VehicleType, FeeStrategy> strategies = new HashMap<>();
    
    /**
     * Constructor injection
     */
    public FeeStrategyFactory(MotorbikeFeeStrategy motorbikeStrategy, CarFeeStrategy carStrategy) {
        // Đăng ký các strategy theo loại xe
        strategies.put(ParkingZone.VehicleType.MOTORBIKE, motorbikeStrategy);
        strategies.put(ParkingZone.VehicleType.CAR, carStrategy);
    }
    
    /**
     * Lấy strategy phù hợp với loại xe
     */
    public FeeStrategy getStrategy(ParkingZone.VehicleType vehicleType) {
        FeeStrategy strategy = strategies.get(vehicleType);
        
        if (strategy == null) {
            throw new IllegalArgumentException("Không tìm thấy chiến lược tính phí cho loại xe: " + vehicleType);
        }
        
        return strategy;
    }
}
