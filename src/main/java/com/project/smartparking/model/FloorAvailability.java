package com.project.smartparking.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "floor_availability", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"floor_id", "vehicle_type"})
})
public class FloorAvailability extends BaseEntity {
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;
    
    @Column(name = "available_spots")
    private Integer availableSpots = 0;
    
    @Column(name = "total_spots")
    private Integer totalSpots = 0;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();
    
    // Constructors
    public FloorAvailability() {}
    
    public FloorAvailability(Floor floor, VehicleType vehicleType, Integer availableSpots, Integer totalSpots) {
        this.floor = floor;
        this.vehicleType = vehicleType;
        this.availableSpots = availableSpots;
        this.totalSpots = totalSpots;
    }
    
    // Getters and Setters
    public Floor getFloor() { return floor; }
    public void setFloor(Floor floor) { this.floor = floor; }
    
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
    
    public Integer getAvailableSpots() { return availableSpots; }
    public void setAvailableSpots(Integer availableSpots) { this.availableSpots = availableSpots; }
    
    public Integer getTotalSpots() { return totalSpots; }
    public void setTotalSpots(Integer totalSpots) { this.totalSpots = totalSpots; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}