package com.project.smartparking.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "parking_spots", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"floor_id", "spot_number"})
})
public class ParkingSpot extends BaseEntity {
    
    @NotNull
    @Column(name = "spot_number", nullable = false)
    private String spotNumber;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "spot_type", nullable = false)
    private VehicleType spotType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SpotStatus status = SpotStatus.AVAILABLE;
    
    @Column(name = "is_available")
    private Boolean isAvailable = true;
    
    @Column(name = "zone")
    private String zone;
    
    @Column(name = "proximity_to_entry")
    private Integer proximityToEntry;
    
    // Relationships
    @OneToMany(mappedBy = "parkingSpot", fetch = FetchType.LAZY)
    private List<ParkingTransaction> parkingTransactions;
    
    // Constructors
    public ParkingSpot() {}
    
    public ParkingSpot(String spotNumber, Floor floor, VehicleType spotType) {
        this.spotNumber = spotNumber;
        this.floor = floor;
        this.spotType = spotType;
        this.isAvailable = true;
        this.status = SpotStatus.AVAILABLE;
    }
    
    // Getters and Setters
    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }
    
    public Floor getFloor() { return floor; }
    public void setFloor(Floor floor) { this.floor = floor; }
    
    public VehicleType getSpotType() { return spotType; }
    public void setSpotType(VehicleType spotType) { this.spotType = spotType; }
    
    public SpotStatus getStatus() { return status; }
    public void setStatus(SpotStatus status) { this.status = status; }
    
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    
    public Integer getProximityToEntry() { return proximityToEntry; }
    public void setProximityToEntry(Integer proximityToEntry) { this.proximityToEntry = proximityToEntry; }
    
    public List<ParkingTransaction> getParkingTransactions() { return parkingTransactions; }
    public void setParkingTransactions(List<ParkingTransaction> parkingTransactions) { this.parkingTransactions = parkingTransactions; }
}