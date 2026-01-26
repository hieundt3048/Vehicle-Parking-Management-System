package com.parking.system.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parking.system.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByIdAndStatus(Long id, Ticket.Status status);
    List<Ticket> findByLicensePlateAndStatus(String licensePlate, Ticket.Status status);
    List<Ticket> findByStatus(Ticket.Status status);
    List<Ticket> findByStatusAndEntryTimeBetween(Ticket.Status status, LocalDateTime start, LocalDateTime end);
    List<Ticket> findByStatusAndExitTimeBetween(Ticket.Status status, LocalDateTime start, LocalDateTime end);
    long countByStatus(Ticket.Status status);
}
