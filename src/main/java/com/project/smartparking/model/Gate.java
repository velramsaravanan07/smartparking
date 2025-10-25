package com.project.smartparking.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "gates")
public class Gate extends BaseEntity {
    
    @NotNull
    @Column(name = "gate_number", nullable = false)
    private String gateNumber;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gate_type", nullable = false)
    private GateType gateType;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;
    
    @Column(name = "is_operational")
    private Boolean isOperational = true;
    
    // Relationships
    @OneToMany(mappedBy = "entryGate", fetch = FetchType.LAZY)
    private List<ParkingTransaction> entryTransactions;
    
    @OneToMany(mappedBy = "exitGate", fetch = FetchType.LAZY)
    private List<ParkingTransaction> exitTransactions;
    
    // Constructors
    public Gate() {}
    
    public Gate(String gateNumber, GateType gateType, Floor floor) {
        this.gateNumber = gateNumber;
        this.gateType = gateType;
        this.floor = floor;
    }
    
    // Getters and Setters
    public String getGateNumber() { return gateNumber; }
    public void setGateNumber(String gateNumber) { this.gateNumber = gateNumber; }
    
    public GateType getGateType() { return gateType; }
    public void setGateType(GateType gateType) { this.gateType = gateType; }
    
    public Floor getFloor() { return floor; }
    public void setFloor(Floor floor) { this.floor = floor; }
    
    public Boolean getIsOperational() { return isOperational; }
    public void setIsOperational(Boolean isOperational) { this.isOperational = isOperational; }
    
    public List<ParkingTransaction> getEntryTransactions() { return entryTransactions; }
    public void setEntryTransactions(List<ParkingTransaction> entryTransactions) { this.entryTransactions = entryTransactions; }
    
    public List<ParkingTransaction> getExitTransactions() { return exitTransactions; }
    public void setExitTransactions(List<ParkingTransaction> exitTransactions) { this.exitTransactions = exitTransactions; }
}