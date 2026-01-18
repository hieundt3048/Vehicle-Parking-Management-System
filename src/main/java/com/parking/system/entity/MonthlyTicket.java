package com.parking.system.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "monthly_tickets")
public class MonthlyTicket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String cardId;
    
    @Column(nullable = false)
    private String licensePlate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParkingZone.VehicleType vehicleType;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Column(nullable = false)
    private Double monthlyFee;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;
    
    public enum Status {
        ACTIVE,
        EXPIRED
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public Double getMonthlyFee() {
        return monthlyFee;
    }
    
    public void setMonthlyFee(Double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
}
