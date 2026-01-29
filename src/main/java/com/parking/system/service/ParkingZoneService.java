package com.parking.system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parking.system.dto.CreateZoneRequest;
import com.parking.system.dto.ZoneWithSlotsResponse;
import com.parking.system.entity.ParkingSlot;
import com.parking.system.entity.ParkingZone;
import com.parking.system.exception.ResourceNotFoundException;
import com.parking.system.repository.ParkingSlotRepository;
import com.parking.system.repository.ParkingZoneRepository;

/**
 * Service xử lý logic liên quan đến Parking Zone
 */
@Service
public class ParkingZoneService {
    
    private final ParkingZoneRepository zoneRepository;
    private final ParkingSlotRepository slotRepository;
    private final ParkingSlotService slotService;
    
    // Constructor injection 
    public ParkingZoneService(ParkingZoneRepository zoneRepository, 
                              ParkingSlotRepository slotRepository,
                              ParkingSlotService slotService) {
        this.zoneRepository = zoneRepository;
        this.slotRepository = slotRepository;
        this.slotService = slotService;
    }
    
    /**
     * Tạo khu vực đỗ xe mới và tự động tạo các slot
     */
    @Transactional
    public ParkingZone createZone(CreateZoneRequest request) {
        // Tạo zone mới
        ParkingZone zone = new ParkingZone();
        zone.setName(request.getName());
        zone.setVehicleType(request.getVehicleType());
        zone.setTotalSlots(request.getTotalSlots());
        
        // Lưu zone trước
        zone = zoneRepository.save(zone);
        
        // Tạo các slot cho zone
        List<ParkingSlot> slots = createSlotsForZone(zone, request.getTotalSlots());
        
        // Lưu tất cả slots
        slotRepository.saveAll(slots);
        zone.setSlots(slots);
        
        return zone;
    }
    
    /**
     * Helper method: Tạo danh sách slots cho zone
     */
    private List<ParkingSlot> createSlotsForZone(ParkingZone zone, int totalSlots) {
        List<ParkingSlot> slots = new ArrayList<>();
        for (int i = 1; i <= totalSlots; i++) {
            ParkingSlot slot = new ParkingSlot();
            slot.setSlotNumber(zone.getName() + "-" + String.format("%03d", i));
            slot.setZone(zone);
            slot.setStatus(ParkingSlot.Status.AVAILABLE);
            slots.add(slot);
        }
        return slots;
    }
    
    /**
     * Lấy danh sách tất cả các zones
     */
    public List<ParkingZone> getAllZones() {
        return zoneRepository.findAll();
    }
    
    /**
     * Lấy thông tin chi tiết một zone
     */
    public ParkingZone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone không tồn tại với ID: " + id));
    }
    
    /**
     * Lấy thông tin zone kèm theo thống kê slots
     */
    public ZoneWithSlotsResponse getZoneWithSlots(Long zoneId) {
        ParkingZone zone = getZoneById(zoneId);
        List<ParkingSlot> slots = zone.getSlots();
        
        long availableCount = countAvailableSlots(zoneId);
        
        ZoneWithSlotsResponse response = new ZoneWithSlotsResponse();
        response.setId(zone.getId());
        response.setName(zone.getName());
        response.setVehicleType(zone.getVehicleType().toString());
        response.setTotalSlots(zone.getTotalSlots());
        response.setAvailableSlots(availableCount);
        response.setOccupiedSlots(slots.size() - availableCount);
        response.setSlots(slots);
        
        return response;
    }
    
    /**
     * Helper: Đếm số slot available cho việc tạo response
     */
    private long countAvailableSlots(Long zoneId) {
        return slotService.countAvailableSlotsByZone(zoneId);
    }
}
