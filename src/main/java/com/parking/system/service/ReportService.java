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
import com.parking.system.entity.ParkingZone;
import com.parking.system.entity.Ticket;
import com.parking.system.repository.ParkingSlotRepository;
import com.parking.system.repository.ParkingZoneRepository;
import com.parking.system.repository.TicketRepository;

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

	public ShiftRevenueResponse getShiftRevenue(LocalDate date) {
		List<ShiftSummary> summaries = new ArrayList<>();
		summaries.add(buildShiftSummary("Ca sáng", date.atTime(LocalTime.of(6, 0)), date.atTime(LocalTime.of(14, 0))));
		summaries.add(buildShiftSummary("Ca chiều", date.atTime(LocalTime.of(14, 0)), date.atTime(LocalTime.of(22, 0))));
		// Ca đêm kéo dài tới ngày hôm sau 06:00
		summaries.add(buildShiftSummary("Ca đêm", date.atTime(LocalTime.of(22, 0)), date.plusDays(1).atTime(LocalTime.of(6, 0))));
		return new ShiftRevenueResponse(date, summaries);
	}

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
