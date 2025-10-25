package com.project.smartparking.model;


import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ReceiptBreakdown")
public class ReceiptBreakdown {
    private final BigDecimal baseRate;
    private final BigDecimal hourlyCharges;
    private final BigDecimal peakHourSurcharge;
    private final BigDecimal weekendSurcharge;
    private final BigDecimal totalAmount;
    
    public ReceiptBreakdown(BigDecimal baseRate, BigDecimal hourlyCharges, 
                           BigDecimal peakHourSurcharge, BigDecimal weekendSurcharge,
                           BigDecimal totalAmount) {
        this.baseRate = baseRate;
        this.hourlyCharges = hourlyCharges;
        this.peakHourSurcharge = peakHourSurcharge;
        this.weekendSurcharge = weekendSurcharge;
        this.totalAmount = totalAmount;
    }
    
    // Getters
    public BigDecimal getBaseRate() { return baseRate; }
    public BigDecimal getHourlyCharges() { return hourlyCharges; }
    public BigDecimal getPeakHourSurcharge() { return peakHourSurcharge; }
    public BigDecimal getWeekendSurcharge() { return weekendSurcharge; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    
    public String getBreakdownString() {
        return """
               Charge Breakdown:
               - Base Rate: $%.2f
               - Hourly Charges: $%.2f
               - Peak Hour Surcharge: $%.2f
               - Weekend Surcharge: $%.2f
               - Total: $%.2f
               """.formatted(baseRate, hourlyCharges, peakHourSurcharge, weekendSurcharge, totalAmount);
    }
}