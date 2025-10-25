package com.project.smartparking.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "floors", uniqueConstraints = {
    @UniqueConstraint(columnNames = "floor_number")
})
public class Floor extends BaseEntity {
    
    @NotNull
    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;
    
    @Column(name = "name")
    private String name;
    
    @NotNull
    @Column(name = "total_capacity", nullable = false)
    private Integer totalCapacity;
    
    @Column(name = "current_occupancy")
    private Integer currentOccupancy = 0;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    // Relationships
    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ParkingSpot> parkingSpots = new ArrayList<>();
    
    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Gate> gate = new ArrayList<>();
    
    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FloorAvailability> floorAvailabilities = new ArrayList<>();
    
    // Constructors
    public Floor() {}
    
    public Floor(Integer floorNumber, String name, Integer totalCapacity) {
        this.floorNumber = floorNumber;
        this.name = name;
        this.totalCapacity = totalCapacity;
        this.currentOccupancy = 0;
    }
    
    // Getters and Setters
    public Integer getFloorNumber() { return floorNumber; }
    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(Integer totalCapacity) { this.totalCapacity = totalCapacity; }
    
    public Integer getCurrentOccupancy() { return currentOccupancy; }
    public void setCurrentOccupancy(Integer currentOccupancy) { this.currentOccupancy = currentOccupancy; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public List<ParkingSpot> getParkingSpots() { return parkingSpots; }
    public void setParkingSpots(List<ParkingSpot> parkingSpots) { this.parkingSpots = parkingSpots; }
    
    public List<Gate> getGates() { return gate; }
    public void setGates(List<Gate> gate) { this.gate = gate; }
    
    public List<FloorAvailability> getFloorAvailabilities() { return floorAvailabilities; }
    public void setFloorAvailabilities(List<FloorAvailability> floorAvailabilities) { this.floorAvailabilities = floorAvailabilities; }
}