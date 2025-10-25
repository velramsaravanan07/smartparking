package com.project.smartparking.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.project.smartparking.model.VehicleType;

public class FloorAvailabilityResponse {
	  private Long floorId;
	    private String floorName;
	    private Map<VehicleType, Integer> availabilityByType;
	    private LocalDateTime timestamp;
	    
	    public FloorAvailabilityResponse(Long floorId, String floorName, Map<VehicleType, Integer> availabilityByType) {
	        this.floorId = floorId;
	        this.floorName = floorName;
	        this.availabilityByType = availabilityByType;
	        this.timestamp = LocalDateTime.now();
	    }
	    
	    // getters
	    public Long getFloorId() { return floorId; }
	    public String getFloorName() { return floorName; }
	    public Map<VehicleType, Integer> getAvailabilityByType() { return availabilityByType; }
	    public LocalDateTime getTimestamp() { return timestamp; }
	}
