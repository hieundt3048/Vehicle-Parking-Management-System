package com.parking.system.service.factory;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.parking.system.entity.ParkingSlot;
import com.parking.system.entity.ParkingZone;
import com.parking.system.entity.Ticket;

/**
 * Implementation chuẩn của TicketFactory
 * Tạo vé thông thường (Standard Ticket) với trạng thái ACTIVE
 */
@Component
public class StandardTicketFactory implements TicketFactory {
    
    @Override
    public Ticket createTicket(String licensePlate, ParkingZone.VehicleType vehicleType, 
                               ParkingSlot slot, LocalDateTime entryTime) {
        // Validate tham số đầu vào
        validateParameters(licensePlate, vehicleType, slot, entryTime);
        
        // Khởi tạo Ticket với các giá trị chuẩn
        Ticket ticket = new Ticket();
        ticket.setLicensePlate(licensePlate.trim().toUpperCase()); // Chuẩn hóa biển số
        ticket.setVehicleType(vehicleType);
        ticket.setSlot(slot);
        ticket.setEntryTime(entryTime);
        ticket.setStatus(Ticket.Status.ACTIVE); // Vé mới luôn ở trạng thái ACTIVE
        
        // exitTime và totalAmount sẽ được set khi checkout
        
        return ticket;
    }
    
    /**
     * Validate các tham số đầu vào
     */
    private void validateParameters(String licensePlate, ParkingZone.VehicleType vehicleType, 
                                    ParkingSlot slot, LocalDateTime entryTime) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("Biển số xe không được để trống");
        }
        
        if (vehicleType == null) {
            throw new IllegalArgumentException("Loại xe không được null");
        }
        
        if (slot == null) {
            throw new IllegalArgumentException("Slot đỗ xe không được null");
        }
        
        if (entryTime == null) {
            throw new IllegalArgumentException("Thời gian vào không được null");
        }
    }
}
