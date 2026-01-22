package com.parking.system.dto;

public class MonthlyRevenueResponse {
    private final int year;
    private final int month;
    private final double revenue;
    private final int ticketCount;

    public MonthlyRevenueResponse(int year, int month, double revenue, int ticketCount) {
        this.year = year;
        this.month = month;
        this.revenue = revenue;
        this.ticketCount = ticketCount;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public double getRevenue() {
        return revenue;
    }

    public int getTicketCount() {
        return ticketCount;
    }
}
