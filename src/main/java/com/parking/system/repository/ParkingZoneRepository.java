package com.parking.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parking.system.entity.ParkingZone;

@Repository
public interface ParkingZoneRepository extends JpaRepository<ParkingZone, Long> {
}
