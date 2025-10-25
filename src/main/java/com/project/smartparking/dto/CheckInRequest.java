package com.project.smartparking.dto;

import com.project.smartparking.model.VehicleType;

public class CheckInRequest {
    private String licensePlate;
    private VehicleType vehicleType;
    private Long entryGateId;
    
    // getters and setters
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
    
    public Long getEntryGateId() { return entryGateId; }
    public void setEntryGateId(Long entryGateId) { this.entryGateId = entryGateId; }
}