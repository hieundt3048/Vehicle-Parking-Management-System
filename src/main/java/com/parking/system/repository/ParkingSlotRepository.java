package com.parking.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.parking.system.entity.ParkingSlot;

import jakarta.persistence.LockModeType;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByZoneIdAndStatus(Long zoneId, ParkingSlot.Status status);
    List<ParkingSlot> findByStatus(ParkingSlot.Status status);
    
    /**
     * Tìm slot trống đầu tiên trong zone cụ thể với PESSIMISTIC WRITE LOCK.
     *
     * Sử dụng query dạng phương thức (derived query) để Spring Data JPA
     * tự sinh JPQL với ORDER BY và giới hạn 1 bản ghi, tránh lỗi
     * "Query did not return a unique result" và vẫn hỗ trợ lock.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ParkingSlot> findFirstByZoneIdAndStatusOrderBySlotNumberAsc(Long zoneId, ParkingSlot.Status status);

    long countByStatus(ParkingSlot.Status status);
    long countByZoneIdAndStatus(Long zoneId, ParkingSlot.Status status);
}
