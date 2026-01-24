package com.parking.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parking.system.dto.ApiResponse;
import com.parking.system.dto.CreateTicketRequest;
import com.parking.system.entity.Ticket;
import com.parking.system.service.TicketService;

/**
 * Ticket Controller - Quản lý vé xe vào/ra
 * Endpoints: /api/tickets
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    /**
     * Tạo vé mới (xe vào)
     * POST /api/tickets
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<Ticket>> createTicket(@RequestBody CreateTicketRequest request) {
        try {
            Ticket ticket = ticketService.createTicket(request);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Tạo vé thành công", ticket)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    /**
     * Xử lý xe ra (checkout)
     * POST /api/tickets/{ticketId}/checkout
     */
    @PostMapping("/{ticketId}/checkout")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<Ticket>> checkoutTicket(@PathVariable Long ticketId) {
        try {
            Ticket ticket = ticketService.processExit(ticketId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Thanh toán thành công", ticket)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    /**
     * Lấy tất cả vé
     * GET /api/tickets
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<Ticket>>> getAllTickets() {
        try {
            List<Ticket> tickets = ticketService.getAllTickets();
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Lấy danh sách vé thành công", tickets)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    /**
     * Lấy tất cả vé đang ACTIVE
     * GET /api/tickets/active
     */
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<Ticket>>> getActiveTickets() {
        try {
            List<Ticket> tickets = ticketService.getActiveTickets();
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Lấy danh sách vé active thành công", tickets)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    /**
     * Tìm kiếm vé theo biển số xe (cho check-out)
     * GET /api/tickets/search?plateNumber={plateNumber}
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<Ticket>> searchTicketByPlate(@RequestParam String plateNumber) {
        try {
            Ticket ticket = ticketService.getActiveTicketByLicensePlate(plateNumber);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Tìm thấy vé", ticket)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }
}
