package com.parking.system.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.parking.system.dto.DailyRevenueResponse;
import com.parking.system.dto.MonthlyRevenueResponse;
import com.parking.system.dto.OccupancyStatsResponse;
import com.parking.system.dto.OccupancyStatsResponse.ZoneOccupancy;
import com.parking.system.dto.OverviewReportResponse;
import com.parking.system.dto.ShiftRevenueResponse;
import com.parking.system.dto.ShiftRevenueResponse.ShiftSummary;
import com.parking.system.entity.ParkingSlot;
import com.parking.system.entity.Ticket;
import com.parking.system.repository.ParkingSlotRepository;
import com.parking.system.repository.ParkingZoneRepository;
import com.parking.system.repository.TicketRepository;

/**
 * Service xử lý các báo cáo thống kê
 * - Doanh thu theo ngày, tháng, ca làm việc
 * - Tình trạng lấp đầy bãi xe
 * - Tổng quan hệ thống
 */
@Service
public class ReportService {
	private final TicketRepository ticketRepository;
	private final ParkingSlotRepository slotRepository;
	private final ParkingZoneRepository zoneRepository;

	public ReportService(TicketRepository ticketRepository,
						 ParkingSlotRepository slotRepository,
						 ParkingZoneRepository zoneRepository) {
		this.ticketRepository = ticketRepository;
		this.slotRepository = slotRepository;
		this.zoneRepository = zoneRepository;
	}

	/**
	 * Báo cáo doanh thu theo ngày
	 * Tính tổng tiền từ các vé đã thanh toán (COMPLETED) trong ngày
	 */
	public DailyRevenueResponse getDailyRevenue(LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();
		List<Ticket> tickets = ticketRepository.findByStatusAndExitTimeBetween(
			Ticket.Status.COMPLETED,
			start,
			end
		);

		double revenue = tickets.stream()
			.mapToDouble(t -> t.getTotalAmount() != null ? t.getTotalAmount() : 0.0)
			.sum();

		return new DailyRevenueResponse(date, revenue, tickets.size());
	}

	/**
	 * Báo cáo doanh thu theo tháng
	 * Tính tổng tiền từ các vé đã thanh toán trong tháng
	 */
	public MonthlyRevenueResponse getMonthlyRevenue(int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime end = yearMonth.plusMonths(1).atDay(1).atStartOfDay();
		List<Ticket> tickets = ticketRepository.findByStatusAndExitTimeBetween(
			Ticket.Status.COMPLETED,
			start,
			end
		);

		double revenue = tickets.stream()
			.mapToDouble(t -> t.getTotalAmount() != null ? t.getTotalAmount() : 0.0)
			.sum();

		return new MonthlyRevenueResponse(year, month, revenue, tickets.size());
	}

	/**
	 * Báo cáo doanh thu theo ca làm việc
	 * - Ca sáng: 6h-14h
	 * - Ca chiều: 14h-22h
	 * - Ca đêm: 22h-6h sáng hôm sau
	 */
	public ShiftRevenueResponse getShiftRevenue(LocalDate date) {
		List<ShiftSummary> summaries = new ArrayList<>();
		summaries.add(buildShiftSummary("Ca sáng", date.atTime(LocalTime.of(6, 0)), date.atTime(LocalTime.of(14, 0))));
		summaries.add(buildShiftSummary("Ca chiều", date.atTime(LocalTime.of(14, 0)), date.atTime(LocalTime.of(22, 0))));
		// Ca đêm kéo dài tới ngày hôm sau 06:00
		summaries.add(buildShiftSummary("Ca đêm", date.atTime(LocalTime.of(22, 0)), date.plusDays(1).atTime(LocalTime.of(6, 0))));
		return new ShiftRevenueResponse(date, summaries);
	}

	/**
	 * Thống kê tình trạng lấp đầy bãi xe
	 * Hiển thị số slot trống/đã đỗ của từng zone và tổng thể
	 */
	public OccupancyStatsResponse getOccupancyStats() {
		long occupied = slotRepository.countByStatus(ParkingSlot.Status.OCCUPIED);
		long available = slotRepository.countByStatus(ParkingSlot.Status.AVAILABLE);
		long total = occupied + available;

		List<ZoneOccupancy> zoneStats = new ArrayList<>();
		zoneRepository.findAll().forEach(zone -> {
			long zoneOccupied = slotRepository.countByZoneIdAndStatus(zone.getId(), ParkingSlot.Status.OCCUPIED);
			long zoneAvailable = slotRepository.countByZoneIdAndStatus(zone.getId(), ParkingSlot.Status.AVAILABLE);
			zoneStats.add(new ZoneOccupancy(
				zone.getId(),
				zone.getName(),
				zone.getVehicleType(),
				zone.getTotalSlots(),
				zoneOccupied,
				zoneAvailable
			));
		});

		return new OccupancyStatsResponse(total, occupied, available, zoneStats);
	}

	/**
	 * Báo cáo tổng quan hệ thống
	 * - Tổng số vé đã tạo
	 * - Số xe đang gửi trong bãi
	 * - Số xe đã xuất bãi hôm nay
	 */
	public OverviewReportResponse getOverview(LocalDate today) {
		long totalTickets = ticketRepository.count();
		long activeTickets = ticketRepository.countByStatus(Ticket.Status.ACTIVE);
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.plusDays(1).atStartOfDay();
		long completedToday = ticketRepository.findByStatusAndExitTimeBetween(
			Ticket.Status.COMPLETED,
			start,
			end
		).size();
		return new OverviewReportResponse(totalTickets, activeTickets, completedToday);
	}

	/**
	 * Helper: Tạo summary cho một ca làm việc
	 * Tính doanh thu và số lượng vé trong khoảng thời gian
	 */
	private ShiftSummary buildShiftSummary(String name, LocalDateTime start, LocalDateTime end) {
		List<Ticket> tickets = ticketRepository.findByStatusAndExitTimeBetween(
			Ticket.Status.COMPLETED,
			start,
			end
		);
		double revenue = tickets.stream()
			.mapToDouble(t -> t.getTotalAmount() != null ? t.getTotalAmount() : 0.0)
			.sum();
		return new ShiftSummary(name, start.toLocalTime().toString(), end.toLocalTime().toString(), revenue, tickets.size());
	}
}
