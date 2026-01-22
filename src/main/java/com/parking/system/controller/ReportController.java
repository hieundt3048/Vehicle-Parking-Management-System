package com.parking.system.controller;

import java.time.LocalDate;
<<<<<<< HEAD

=======
<<<<<<< HEAD
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
=======

>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parking.system.dto.ApiResponse;
<<<<<<< HEAD
=======
<<<<<<< HEAD
import com.parking.system.entity.Ticket;
import com.parking.system.repository.TicketRepository;
=======
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
import com.parking.system.dto.DailyRevenueResponse;
import com.parking.system.dto.MonthlyRevenueResponse;
import com.parking.system.dto.OccupancyStatsResponse;
import com.parking.system.dto.OverviewReportResponse;
import com.parking.system.dto.ShiftRevenueResponse;
import com.parking.system.service.ReportService;
<<<<<<< HEAD
=======
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    
<<<<<<< HEAD
=======
<<<<<<< HEAD
    @Autowired
    private TicketRepository ticketRepository;
    
=======
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
    /**
     * API lấy doanh thu theo ngày
     * GET /api/reports/revenue/daily?date=2024-01-18
     */
    @GetMapping("/revenue/daily")
<<<<<<< HEAD
=======
<<<<<<< HEAD
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDailyRevenue(@RequestParam String date) {
        try {
            LocalDate targetDate = LocalDate.parse(date);
            LocalDateTime startOfDay = targetDate.atStartOfDay();
            LocalDateTime endOfDay = targetDate.plusDays(1).atStartOfDay();
            
            // Lấy tất cả tickets đã completed trong ngày
            var tickets = ticketRepository.findByStatusAndEntryTimeBetween(
                Ticket.Status.COMPLETED,
                startOfDay,
                endOfDay
            );
            
            // Tính tổng doanh thu
            double totalRevenue = tickets.stream()
                .mapToDouble(t -> t.getTotalAmount() != null ? t.getTotalAmount() : 0.0)
                .sum();
            
            Map<String, Object> result = new HashMap<>();
            result.put("date", date);
            result.put("revenue", totalRevenue);
            result.put("ticketCount", tickets.size());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy doanh thu thành công", result));
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("date", date);
            result.put("revenue", 0.0);
            result.put("ticketCount", 0);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy doanh thu thành công", result));
        }
=======
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
    public ResponseEntity<ApiResponse<DailyRevenueResponse>> getDailyRevenue(@RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        DailyRevenueResponse response = reportService.getDailyRevenue(targetDate);
        return ResponseEntity.ok(ApiResponse.success("Lấy doanh thu thành công", response));
    }
    
    /**
     * API lấy doanh thu theo tháng
     * GET /api/reports/revenue/monthly?month=1&year=2026
     */
    @GetMapping("/revenue/monthly")
    public ResponseEntity<ApiResponse<MonthlyRevenueResponse>> getMonthlyRevenue(
            @RequestParam int month,
            @RequestParam int year) {
        MonthlyRevenueResponse response = reportService.getMonthlyRevenue(year, month);
        return ResponseEntity.ok(ApiResponse.success("Lấy doanh thu theo tháng thành công", response));
    }

    /**
     * API lấy doanh thu theo ca làm việc
     * GET /api/reports/revenue/shifts?date=2026-01-18
     */
    @GetMapping("/revenue/shifts")
    public ResponseEntity<ApiResponse<ShiftRevenueResponse>> getShiftRevenue(
            @RequestParam(required = false) String date) {
        LocalDate targetDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        ShiftRevenueResponse response = reportService.getShiftRevenue(targetDate);
        return ResponseEntity.ok(ApiResponse.success("Lấy doanh thu theo ca thành công", response));
    }

    /**
     * API thống kê hiện trạng bãi xe
     */
    @GetMapping("/occupancy")
    public ResponseEntity<ApiResponse<OccupancyStatsResponse>> getOccupancyStats() {
        OccupancyStatsResponse response = reportService.getOccupancyStats();
        return ResponseEntity.ok(ApiResponse.success("Lấy trạng thái bãi xe thành công", response));
<<<<<<< HEAD
=======
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
    }
    
    /**
     * API tổng hợp báo cáo
     * GET /api/reports
     */
    @GetMapping
<<<<<<< HEAD
    public ResponseEntity<ApiResponse<OverviewReportResponse>> getGeneralReport() {
        OverviewReportResponse response = reportService.getOverview(LocalDate.now());
        return ResponseEntity.ok(ApiResponse.success("Lấy báo cáo thành công", response));
=======
<<<<<<< HEAD
    public ResponseEntity<ApiResponse<Map<String, Object>>> getGeneralReport() {
        try {
            // Thống kê tổng quan
            Map<String, Object> report = new HashMap<>();
            report.put("totalTickets", ticketRepository.count());
            report.put("activeTickets", ticketRepository.findByIdAndStatus(null, Ticket.Status.ACTIVE).map(t -> 1L).orElse(0L));
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy báo cáo thành công", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
=======
    public ResponseEntity<ApiResponse<OverviewReportResponse>> getGeneralReport() {
        OverviewReportResponse response = reportService.getOverview(LocalDate.now());
        return ResponseEntity.ok(ApiResponse.success("Lấy báo cáo thành công", response));
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
    }
}

