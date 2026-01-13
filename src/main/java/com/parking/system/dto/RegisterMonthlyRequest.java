package com.parking.system.dto;

import com.parking.system.entity.ParkingZone;

public class RegisterMonthlyRequest {
    private String cardId;
    private String licensePlate;
    private ParkingZone.VehicleType vehicleType;
    private Double monthlyFee;
    
    public String getCardId() {
        return cardId;
    }
    
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    public ParkingZone.VehicleType getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(ParkingZone.VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public Double getMonthlyFee() {
        return monthlyFee;
    }
    
    public void setMonthlyFee(Double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }
}
