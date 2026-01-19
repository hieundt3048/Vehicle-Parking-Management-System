package com.parking.system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 * Controller xử lý các API liên quan đến Ticket (vé xe)
 * Tuân thủ Single Responsibility: chỉ xử lý HTTP requests/responses
 * Business logic được delegate cho TicketService
 */
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class TicketController {

    private final TicketService ticketService;
    
    // Constructor injection - tuân thủ Dependency Inversion Principle
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    
    /**
     * API tạo vé mới (Check-in)
     * POST /api/tickets
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Ticket>> createTicket(@RequestBody CreateTicketRequest request) {
        Ticket ticket = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, "Tạo vé thành công", ticket));
    }
    
    /**
     * API xử lý xuất bãi (Check-out)
     * POST /api/tickets/{id}/checkout
     */
    @PostMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse<Ticket>> checkoutTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.processExit(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xuất bãi thành công", ticket));
    }
    
    /**
     * API lấy tất cả vé
     * GET /api/tickets
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Ticket>>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách vé thành công", tickets));
    }
    
    /**
     * API lấy vé theo ID
     * GET /api/tickets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Ticket>> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy thông tin vé thành công", ticket));
    }
    
    /**
     * API tìm vé theo biển số
     * GET /api/tickets/search?plateNumber={plateNumber}
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Ticket>> searchByPlateNumber(@RequestParam String plateNumber) {
        Ticket ticket = ticketService.getActiveTicketByLicensePlate(plateNumber);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tìm thấy vé", ticket));
    }
    
    /**
     * API lấy danh sách vé đang hoạt động
     * GET /api/tickets/active
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Ticket>>> getActiveTickets() {
        List<Ticket> tickets = ticketService.getActiveTickets();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách vé đang hoạt động thành công", tickets));
    }
}
