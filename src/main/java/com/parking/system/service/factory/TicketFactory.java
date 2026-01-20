package com.parking.system.service.factory;

import java.time.LocalDateTime;

import com.parking.system.entity.ParkingSlot;
import com.parking.system.entity.ParkingZone;
import com.parking.system.entity.Ticket;

/**
 * Factory Method Pattern cho việc tạo đối tượng Ticket
 */
public interface TicketFactory {
    
    /**
     * Tạo đối tượng Ticket mới với các thông tin cần thiết
     */
    Ticket createTicket(String licensePlate, ParkingZone.VehicleType vehicleType, 
                       ParkingSlot slot, LocalDateTime entryTime);
}
