package com.project.smartparking.dto;

public class CheckOutRequest {
	 private String licensePlate;
	    private Long exitGateId;
	    
	    // getters and setters
	    public String getLicensePlate() { return licensePlate; }
	    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
	    
	    public Long getExitGateId() { return exitGateId; }
	    public void setExitGateId(Long exitGateId) { this.exitGateId = exitGateId; }
	}
