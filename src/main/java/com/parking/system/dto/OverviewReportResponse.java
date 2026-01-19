package com.parking.system.dto;

public class OverviewReportResponse {
    private final long totalTickets;
    private final long activeTickets;
    private final long completedToday;

    public OverviewReportResponse(long totalTickets, long activeTickets, long completedToday) {
        this.totalTickets = totalTickets;
        this.activeTickets = activeTickets;
        this.completedToday = completedToday;
    }

    public long getTotalTickets() {
        return totalTickets;
    }

    public long getActiveTickets() {
        return activeTickets;
    }

    public long getCompletedToday() {
        return completedToday;
    }
}
