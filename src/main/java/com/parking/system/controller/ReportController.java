package com.parking.system.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parking.system.dto.ApiResponse;
import com.parking.system.dto.DailyRevenueResponse;
import com.parking.system.dto.MonthlyRevenueResponse;
import com.parking.system.dto.OccupancyStatsResponse;
import com.parking.system.dto.OverviewReportResponse;
import com.parking.system.dto.ShiftRevenueResponse;
import com.parking.system.service.ReportService;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    
    /**
     * API lấy doanh thu theo ngày
     * GET /api/reports/revenue/daily?date=2024-01-18
     */
    @GetMapping("/revenue/daily")
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
    }
    
    /**
     * API tổng hợp báo cáo
     * GET /api/reports
     */
    @GetMapping
    public ResponseEntity<ApiResponse<OverviewReportResponse>> getGeneralReport() {
        OverviewReportResponse response = reportService.getOverview(LocalDate.now());
        return ResponseEntity.ok(ApiResponse.success("Lấy báo cáo thành công", response));
    }
}

