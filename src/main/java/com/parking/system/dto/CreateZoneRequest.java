package com.parking.system.dto;

import com.parking.system.entity.ParkingZone;

public class CreateZoneRequest {
    private String name;
    private ParkingZone.VehicleType vehicleType;
    private Integer totalSlots;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ParkingZone.VehicleType getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(ParkingZone.VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public Integer getTotalSlots() {
        return totalSlots;
    }
    
    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }
}
