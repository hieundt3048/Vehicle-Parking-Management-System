package com.parking.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.parking.system.entity.ParkingSlot;

import jakarta.persistence.LockModeType;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByZoneIdAndStatus(Long zoneId, ParkingSlot.Status status);
    List<ParkingSlot> findByStatus(ParkingSlot.Status status);
    
    /**
     * Tìm slot trống đầu tiên trong zone cụ thể với PESSIMISTIC WRITE LOCK
     * Sử dụng pessimistic lock để tránh race condition khi nhiều request đồng thời
     * cùng cố gắng cấp phát slot trống
     * 
     * PESSIMISTIC_WRITE: Database sẽ lock row này cho đến khi transaction kết thúc
     * Các thread khác phải đợi lock được release
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ps FROM ParkingSlot ps WHERE ps.zone.id = :zoneId AND ps.status = :status ORDER BY ps.slotNumber ASC")
    Optional<ParkingSlot> findFirstByZoneIdAndStatusWithLock(@Param("zoneId") Long zoneId, @Param("status") ParkingSlot.Status status);

    long countByStatus(ParkingSlot.Status status);
    long countByZoneIdAndStatus(Long zoneId, ParkingSlot.Status status);
}
