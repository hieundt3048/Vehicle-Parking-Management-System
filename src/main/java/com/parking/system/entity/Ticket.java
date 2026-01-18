package com.parking.system.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String licensePlate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParkingZone.VehicleType vehicleType;
    
    @Column(nullable = false)
    private LocalDateTime entryTime;
    
    private LocalDateTime exitTime;
    
    @ManyToOne
    @JoinColumn(name = "slot_id")
    private ParkingSlot slot;
    
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;
    
    public enum Status {
        ACTIVE,
        COMPLETED
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    
    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
    
    public LocalDateTime getExitTime() {
        return exitTime;
    }
    
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
    
    public ParkingSlot getSlot() {
        return slot;
    }
    
    public void setSlot(ParkingSlot slot) {
        this.slot = slot;
    }
    
    public Double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
}
