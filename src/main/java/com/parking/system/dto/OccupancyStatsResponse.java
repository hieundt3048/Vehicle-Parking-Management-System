package com.parking.system.dto;

import java.util.List;

import com.parking.system.entity.ParkingZone;

public class OccupancyStatsResponse {
    private final long totalSlots;
    private final long occupiedSlots;
    private final long availableSlots;
    private final List<ZoneOccupancy> zones;

    public OccupancyStatsResponse(long totalSlots, long occupiedSlots, long availableSlots, List<ZoneOccupancy> zones) {
        this.totalSlots = totalSlots;
        this.occupiedSlots = occupiedSlots;
        this.availableSlots = availableSlots;
        this.zones = zones;
    }

    public long getTotalSlots() {
        return totalSlots;
    }

    public long getOccupiedSlots() {
        return occupiedSlots;
    }

    public long getAvailableSlots() {
        return availableSlots;
    }

    public List<ZoneOccupancy> getZones() {
        return zones;
    }

    public static class ZoneOccupancy {
        private final Long zoneId;
        private final String zoneName;
        private final ParkingZone.VehicleType vehicleType;
        private final int totalSlots;
        private final long occupiedSlots;
        private final long availableSlots;

        public ZoneOccupancy(Long zoneId, String zoneName, ParkingZone.VehicleType vehicleType,
                             int totalSlots, long occupiedSlots, long availableSlots) {
            this.zoneId = zoneId;
            this.zoneName = zoneName;
            this.vehicleType = vehicleType;
            this.totalSlots = totalSlots;
            this.occupiedSlots = occupiedSlots;
            this.availableSlots = availableSlots;
        }

        public Long getZoneId() {
            return zoneId;
        }

        public String getZoneName() {
            return zoneName;
        }

        public ParkingZone.VehicleType getVehicleType() {
            return vehicleType;
        }

        public int getTotalSlots() {
            return totalSlots;
        }

        public long getOccupiedSlots() {
            return occupiedSlots;
        }

        public long getAvailableSlots() {
            return availableSlots;
        }
    }
}
