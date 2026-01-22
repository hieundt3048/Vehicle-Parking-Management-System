package com.parking.system.dto;

import java.time.LocalDate;
import java.util.List;

public class ShiftRevenueResponse {
    private final LocalDate date;
    private final List<ShiftSummary> shifts;

    public ShiftRevenueResponse(LocalDate date, List<ShiftSummary> shifts) {
        this.date = date;
        this.shifts = shifts;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<ShiftSummary> getShifts() {
        return shifts;
    }

    public static class ShiftSummary {
        private final String name;
        private final String start;
        private final String end;
        private final double revenue;
        private final int ticketCount;

        public ShiftSummary(String name, String start, String end, double revenue, int ticketCount) {
            this.name = name;
            this.start = start;
            this.end = end;
            this.revenue = revenue;
            this.ticketCount = ticketCount;
        }

        public String getName() {
            return name;
        }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }

        public double getRevenue() {
            return revenue;
        }

        public int getTicketCount() {
            return ticketCount;
        }
    }
}
