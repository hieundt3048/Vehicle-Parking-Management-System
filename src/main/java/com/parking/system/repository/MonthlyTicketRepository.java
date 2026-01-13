package com.parking.system.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parking.system.entity.MonthlyTicket;

@Repository
public interface MonthlyTicketRepository extends JpaRepository<MonthlyTicket, Long> {
    Optional<MonthlyTicket> findByCardIdAndStatus(String cardId, MonthlyTicket.Status status);
    Optional<MonthlyTicket> findByLicensePlateAndStatus(String licensePlate, MonthlyTicket.Status status);
    List<MonthlyTicket> findByStatusAndEndDateBefore(MonthlyTicket.Status status, LocalDate date);
}
