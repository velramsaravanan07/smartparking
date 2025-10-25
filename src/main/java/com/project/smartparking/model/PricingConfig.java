package com.project.smartparking.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "pricing_config", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"vehicle_type", "effective_from"})
})
public class PricingConfig extends BaseEntity {
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;
    
    @NotNull
    @Column(name = "base_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseRate;
    
    @NotNull
    @Column(name = "hourly_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;
    
    @Column(name = "daily_rate", precision = 10, scale = 2)
    private BigDecimal dailyRate;
    
    @Column(name = "weekly_rate", precision = 10, scale = 2)
    private BigDecimal weeklyRate;
    
    @Column(name = "monthly_rate", precision = 10, scale = 2)
    private BigDecimal monthlyRate;
    
    @Column(name = "peak_hour_surcharge", precision = 10, scale = 2)
    private BigDecimal peakHourSurcharge = BigDecimal.ZERO;
    
    @Column(name = "peak_start_time")
    private LocalTime peakStartTime = LocalTime.of(8, 0);
    
    @Column(name = "peak_end_time")
    private LocalTime peakEndTime = LocalTime.of(18, 0);
    
    @Column(name = "weekend_surcharge", precision = 10, scale = 2)
    private BigDecimal weekendSurcharge = BigDecimal.ZERO;
    
    @Column(name = "long_term_discount", precision = 5, scale = 2)
    private BigDecimal longTermDiscount = BigDecimal.ZERO;
    
    @Column(name = "long_term_threshold_days")
    private Integer longTermThresholdDays = 7;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "effective_from")
    private LocalDate effectiveFrom = LocalDate.now();
    
    @Column(name = "effective_to")
    private LocalDate effectiveTo;
    
    // Constructors
    public PricingConfig() {}
    
    public PricingConfig(VehicleType vehicleType, BigDecimal baseRate, BigDecimal hourlyRate) {
        this.vehicleType = vehicleType;
        this.baseRate = baseRate;
        this.hourlyRate = hourlyRate;
    }
    
    // Getters and Setters
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
    
    public BigDecimal getBaseRate() { return baseRate; }
    public void setBaseRate(BigDecimal baseRate) { this.baseRate = baseRate; }
    
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    
    public BigDecimal getWeeklyRate() { return weeklyRate; }
    public void setWeeklyRate(BigDecimal weeklyRate) { this.weeklyRate = weeklyRate; }
    
    public BigDecimal getMonthlyRate() { return monthlyRate; }
    public void setMonthlyRate(BigDecimal monthlyRate) { this.monthlyRate = monthlyRate; }
    
    public BigDecimal getPeakHourSurcharge() { return peakHourSurcharge; }
    public void setPeakHourSurcharge(BigDecimal peakHourSurcharge) { this.peakHourSurcharge = peakHourSurcharge; }
    
    public LocalTime getPeakStartTime() { return peakStartTime; }
    public void setPeakStartTime(LocalTime peakStartTime) { this.peakStartTime = peakStartTime; }
    
    public LocalTime getPeakEndTime() { return peakEndTime; }
    public void setPeakEndTime(LocalTime peakEndTime) { this.peakEndTime = peakEndTime; }
    
    public BigDecimal getWeekendSurcharge() { return weekendSurcharge; }
    public void setWeekendSurcharge(BigDecimal weekendSurcharge) { this.weekendSurcharge = weekendSurcharge; }
    
    public BigDecimal getLongTermDiscount() { return longTermDiscount; }
    public void setLongTermDiscount(BigDecimal longTermDiscount) { this.longTermDiscount = longTermDiscount; }
    
    public Integer getLongTermThresholdDays() { return longTermThresholdDays; }
    public void setLongTermThresholdDays(Integer longTermThresholdDays) { this.longTermThresholdDays = longTermThresholdDays; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
}