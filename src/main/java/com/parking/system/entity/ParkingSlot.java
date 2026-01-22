package com.parking.system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "parking_slots")
public class ParkingSlot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String slotNumber;
    
    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    @JsonBackReference
    private ParkingZone zone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.AVAILABLE;
    
    public enum Status {
        AVAILABLE,
        OCCUPIED
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSlotNumber() {
        return slotNumber;
    }
    
    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }
    
    public ParkingZone getZone() {
        return zone;
    }
    
    public void setZone(ParkingZone zone) {
        this.zone = zone;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
}
