package com.parking.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parking.system.config.FeeConfiguration;
import com.parking.system.dto.ApiResponse;
import com.parking.system.dto.FeeConfigResponse;

/**
 * Controller để expose thông tin cấu hình phí
 * Giúp Frontend lấy giá động thay vì hardcode
 */
@RestController
@RequestMapping("/api/fees")
public class FeeController {
    
    private final FeeConfiguration feeConfig;
    
    public FeeController(FeeConfiguration feeConfig) {
        this.feeConfig = feeConfig;
    }
    
    /**
     * API: Lấy cấu hình phí hiện tại
     * GET /api/fees/config
     * Không cần authentication - thông tin công khai
     */
    @GetMapping("/config")
    public ResponseEntity<ApiResponse<FeeConfigResponse>> getFeeConfig() {
        FeeConfigResponse response = new FeeConfigResponse(
            feeConfig.getMotorbikeFirstBlock(),
            feeConfig.getMotorbikeNextHour(),
            feeConfig.getCarFirstBlock(),
            feeConfig.getCarNextHour(),
            feeConfig.getFirstBlockHours()
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
