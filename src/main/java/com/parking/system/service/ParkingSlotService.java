package com.parking.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parking.system.entity.ParkingSlot;
import com.parking.system.exception.ResourceNotFoundException;
import com.parking.system.repository.ParkingSlotRepository;

/**
 * Service chuyên xử lý logic liên quan đến Parking Slot
 */
@Service
public class ParkingSlotService {
    
    private final ParkingSlotRepository slotRepository;
    
    public ParkingSlotService(ParkingSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }
    
    /**
     * Lấy danh sách các slot đang trống (toàn bộ hệ thống)
     */
    public List<ParkingSlot> getAvailableSlots() {
        return slotRepository.findByStatus(ParkingSlot.Status.AVAILABLE);
    }
    
    /**
     * Lấy danh sách các slot đang trống theo zone
     */
    public List<ParkingSlot> getAvailableSlotsByZone(Long zoneId) {
        return slotRepository.findByZoneIdAndStatus(zoneId, ParkingSlot.Status.AVAILABLE);
    }
    
    /**
     * Cập nhật trạng thái slot
     */
    @Transactional
    public ParkingSlot updateSlotStatus(Long slotId, ParkingSlot.Status newStatus) {
        ParkingSlot slot = getSlotById(slotId);
        slot.setStatus(newStatus);
        return slotRepository.save(slot);
    }
    
    /**
     * Lấy thông tin slot theo ID
     */
    public ParkingSlot getSlotById(Long slotId) {
        return slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot không tồn tại với ID: " + slotId));
    }
    
    /**
     * Đếm số slot available của một zone
     */
    public long countAvailableSlotsByZone(Long zoneId) {
        return slotRepository.findByZoneIdAndStatus(zoneId, ParkingSlot.Status.AVAILABLE).size();
    }
}
