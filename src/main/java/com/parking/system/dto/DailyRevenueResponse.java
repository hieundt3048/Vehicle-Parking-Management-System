package com.parking.system.dto;

import java.time.LocalDate;

public class DailyRevenueResponse {
    private LocalDate date;
    private double revenue;
    private int ticketCount;

    public DailyRevenueResponse(LocalDate date, double revenue, int ticketCount) {
        this.date = date;
        this.revenue = revenue;
        this.ticketCount = ticketCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getRevenue() {
        return revenue;
    }

    public int getTicketCount() {
        return ticketCount;
    }
}
