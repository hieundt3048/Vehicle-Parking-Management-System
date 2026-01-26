package com.parking.system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parking.system.dto.ApiResponse;
import com.parking.system.dto.AvailableSlotsResponse;
import com.parking.system.dto.CreateZoneRequest;
import com.parking.system.dto.UpdateSlotStatusRequest;
import com.parking.system.dto.ZoneWithSlotsResponse;
import com.parking.system.entity.ParkingSlot;
import com.parking.system.entity.ParkingZone;
import com.parking.system.exception.InvalidRequestException;
import com.parking.system.service.ParkingSlotService;
import com.parking.system.service.ParkingZoneService;

/**
 * Controller xử lý các API liên quan đến Parking Zone và Slot
 * Tuân thủ Single Responsibility: chỉ xử lý HTTP requests/responses
 */
@RestController
@RequestMapping("/api/zones")
public class ParkingZoneController {
    
    private final ParkingZoneService zoneService;
    private final ParkingSlotService slotService;
    
    // Constructor injection - tuân thủ Dependency Inversion Principle
    public ParkingZoneController(ParkingZoneService zoneService, ParkingSlotService slotService) {
        this.zoneService = zoneService;
        this.slotService = slotService;
    }
    
    /**
     * API: Tạo khu vực/vị trí đỗ xe mới
     * POST /api/zones
     * Body: { "name": "Khu A", "vehicleType": "MOTORBIKE", "totalSlots": 20 }
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ParkingZone>> createZone(@RequestBody CreateZoneRequest request) {
        ParkingZone zone = zoneService.createZone(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo khu vực thành công", zone));
    }
    
    /**
     * API: Lấy danh sách tất cả các zones
     * GET /api/zones
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ParkingZone>>> getAllZones() {
        List<ParkingZone> zones = zoneService.getAllZones();
        return ResponseEntity.ok(ApiResponse.success(zones));
    }
    
    /**
     * API: Lấy thông tin chi tiết một zone
     * GET /api/zones/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingZone>> getZoneById(@PathVariable Long id) {
        ParkingZone zone = zoneService.getZoneById(id);
        return ResponseEntity.ok(ApiResponse.success(zone));
    }
    
    /**
     * API: Trả về danh sách các slot đang trống
     * GET /api/zones/available-slots
     * Query params: zoneId (optional) - để lọc theo zone
     * 
     * Note: Gọi trực tiếp ParkingSlotService (không qua ZoneService)
     * để tránh coupling không cần thiết
     */
    @GetMapping("/available-slots")
    public ResponseEntity<ApiResponse<AvailableSlotsResponse>> getAvailableSlots(
            @RequestParam(required = false) Long zoneId) {
        
        List<ParkingSlot> availableSlots = (zoneId != null) 
                ? slotService.getAvailableSlotsByZone(zoneId)
                : slotService.getAvailableSlots();
        
        AvailableSlotsResponse response = new AvailableSlotsResponse(
                availableSlots.size(), 
                availableSlots
        );
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * API: Cập nhật trạng thái slot (Available <-> Occupied)
     * PUT /api/zones/slots/{slotId}/status
     * Body: { "status": "OCCUPIED" }
     * 
     * Note: Gọi trực tiếp ParkingSlotService vì đây là slot operation
     */
    @PutMapping("/slots/{slotId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ParkingSlot>> updateSlotStatus(
            @PathVariable Long slotId, 
            @RequestBody UpdateSlotStatusRequest request) {
        
        if (request.getStatus() == null) {
            throw new InvalidRequestException("Trường 'status' là bắt buộc");
        }
        
        ParkingSlot updatedSlot = slotService.updateSlotStatus(slotId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái slot thành công", updatedSlot));
    }
    
    /**
     * API: Lấy tất cả slots của một zone
     * GET /api/zones/{zoneId}/slots
     * 
     * Note: Gọi ZoneService vì cần thông tin zone và thống kê
     */
    @GetMapping("/{zoneId}/slots")
    public ResponseEntity<ApiResponse<ZoneWithSlotsResponse>> getSlotsByZone(@PathVariable Long zoneId) {
        ZoneWithSlotsResponse response = zoneService.getZoneWithSlots(zoneId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
