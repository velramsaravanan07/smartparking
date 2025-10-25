package com.project.smartparking.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.project.smartparking.model.VehicleType;

public class AvailabilityResponse {
	private String message;
    private Map<Long, Map<VehicleType, Integer>> availability;
    private LocalDateTime lastUpdated;
    
    public AvailabilityResponse(String message, Map<Long, Map<VehicleType, Integer>> availability) {
        this.message = message;
        this.availability = availability;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // getters
    public String getMessage() { return message; }
    public Map<Long, Map<VehicleType, Integer>> getAvailability() { return availability; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
}
