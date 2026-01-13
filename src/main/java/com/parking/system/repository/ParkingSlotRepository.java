package com.parking.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parking.system.entity.ParkingSlot;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByZoneIdAndStatus(Long zoneId, ParkingSlot.Status status);
    List<ParkingSlot> findByStatus(ParkingSlot.Status status);
}
