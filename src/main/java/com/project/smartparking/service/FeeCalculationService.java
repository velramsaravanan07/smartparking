package com.project.smartparking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.smartparking.model.Floor;
import com.project.smartparking.model.ParkingReceipt;
import com.project.smartparking.model.ParkingSpot;
import com.project.smartparking.model.ParkingTransaction;
import com.project.smartparking.model.PricingConfig;
import com.project.smartparking.model.ReceiptBreakdown;
import com.project.smartparking.model.Vehicle;
import com.project.smartparking.model.VehicleType;
import com.project.smartparking.repository.FloorRepository;
import com.project.smartparking.repository.ParkingSpotRepository;
import com.project.smartparking.repository.PricingRepository;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;

@Service
public class FeeCalculationService {
    
    @Autowired
    private PricingRepository pricingRepository;
    
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    
    @Autowired
    private FloorRepository floorRepository;
    
    public BigDecimal calculateFee(VehicleType vehicleType, LocalDateTime checkIn, LocalDateTime checkOut) {
        PricingConfig pricing = pricingRepository.findByVehicleType(vehicleType)
            .orElseThrow(() -> new RuntimeException("Pricing not found for vehicle type: " + vehicleType));
        
        Duration duration = Duration.between(checkIn, checkOut);
        long totalMinutes = duration.toMinutes();
        long totalHours = duration.toHours();
        long days = duration.toDays();
        long weeks = days / 7;
        long months = days / 30;
        
        // Calculate base amount based on duration
        BigDecimal totalAmount = calculateBaseAmount(pricing, totalMinutes, totalHours, days, weeks, months);
        
        // Apply peak hour surcharge
        totalAmount = applyPeakHourSurcharge(totalAmount, pricing, checkIn, checkOut);
        
        // Apply weekend surcharge
        totalAmount = applyWeekendSurcharge(totalAmount, pricing, checkIn, checkOut);
        
        // Apply long-term discount
        totalAmount = applyLongTermDiscount(totalAmount, pricing, days);
        
        return totalAmount.max(BigDecimal.ZERO);
    }
    
    private BigDecimal calculateBaseAmount(PricingConfig pricing, long minutes, long hours, long days, long weeks, long months) {
        // Monthly rate
        if (months > 0 && pricing.getMonthlyRate() != null) {
            return pricing.getMonthlyRate().multiply(BigDecimal.valueOf(months))
                   .add(calculateRemainingDays(pricing, days % 30));
        }
        
        // Weekly rate
        if (weeks > 0 && pricing.getWeeklyRate() != null) {
            return pricing.getWeeklyRate().multiply(BigDecimal.valueOf(weeks))
                   .add(calculateRemainingDays(pricing, days % 7));
        }
        
        // Daily rate
        if (days > 0 && pricing.getDailyRate() != null) {
            return pricing.getDailyRate().multiply(BigDecimal.valueOf(days))
                   .add(calculateRemainingHours(pricing, hours % 24, minutes % 60));
        }
        
        // Hourly calculation
        if (hours > 0) {
            BigDecimal amount = pricing.getBaseRate();
            if (hours > 1) {
                amount = amount.add(pricing.getHourlyRate().multiply(BigDecimal.valueOf(hours - 1)));
            }
            // Add partial hour
            if (minutes % 60 > 0) {
                amount = amount.add(pricing.getHourlyRate());
            }
            return amount;
        }
        
        // Less than 1 hour
        return pricing.getBaseRate();
    }
    
    private BigDecimal calculateRemainingDays(PricingConfig pricing, long remainingDays) {
        if (remainingDays == 0) return BigDecimal.ZERO;
        return pricing.getDailyRate() != null ? 
            pricing.getDailyRate().multiply(BigDecimal.valueOf(remainingDays)) :
            pricing.getHourlyRate().multiply(BigDecimal.valueOf(remainingDays * 24));
    }
    
    private BigDecimal calculateRemainingHours(PricingConfig pricing, long hours, long minutes) {
        if (hours == 0 && minutes == 0) return BigDecimal.ZERO;
        
        BigDecimal amount = BigDecimal.ZERO;
        if (hours > 0) {
            amount = pricing.getHourlyRate().multiply(BigDecimal.valueOf(hours));
        }
        if (minutes > 0) {
            amount = amount.add(pricing.getHourlyRate());
        }
        return amount;
    }
    
    private BigDecimal applyPeakHourSurcharge(BigDecimal amount, PricingConfig pricing, 
                                            LocalDateTime checkIn, LocalDateTime checkOut) {
        long peakHours = calculatePeakHours(checkIn, checkOut, pricing);
        return amount.add(pricing.getPeakHourSurcharge().multiply(BigDecimal.valueOf(peakHours)));
    }
    
    private long calculatePeakHours(LocalDateTime checkIn, LocalDateTime checkOut, PricingConfig pricing) {
        long peakHours = 0;
        LocalDateTime current = checkIn;
        
        while (current.isBefore(checkOut)) {
            LocalTime currentTime = current.toLocalTime();
            if (!currentTime.isBefore(pricing.getPeakStartTime()) && 
                !currentTime.isAfter(pricing.getPeakEndTime())) {
                peakHours++;
            }
            current = current.plusHours(1);
            if (current.isAfter(checkOut)) {
                break;
            }
        }
        return peakHours;
    }
    
    private BigDecimal applyWeekendSurcharge(BigDecimal amount, PricingConfig pricing,
                                           LocalDateTime checkIn, LocalDateTime checkOut) {
        long weekendDays = calculateWeekendDays(checkIn, checkOut);
        return amount.add(pricing.getWeekendSurcharge().multiply(BigDecimal.valueOf(weekendDays)));
    }
    
    private long calculateWeekendDays(LocalDateTime checkIn, LocalDateTime checkOut) {
        long weekendDays = 0;
        LocalDate currentDate = checkIn.toLocalDate();
        LocalDate endDate = checkOut.toLocalDate();
        
        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                weekendDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return weekendDays;
    }
    
    private BigDecimal applyLongTermDiscount(BigDecimal amount, PricingConfig pricing, long days) {
        if (days >= pricing.getLongTermThresholdDays() && 
            pricing.getLongTermDiscount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = amount.multiply(pricing.getLongTermDiscount())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            return amount.subtract(discount);
        }
        return amount;
    }
    
    public ParkingReceipt generateReceipt(ParkingTransaction transaction, Vehicle vehicle) {
        // Calculate duration
        Duration duration = Duration.between(transaction.getCheckInTime(), transaction.getCheckOutTime());
        long durationMinutes = duration.toMinutes();
        
        // Get parking spot details
        ParkingSpot spot = parkingSpotRepository.findById(transaction.getId())
            .orElseThrow(() -> new RuntimeException("Parking spot not found: " + transaction.getId()));
        
        // Get floor information
        Floor floor = floorRepository.findById(spot.getId())
            .orElseThrow(() -> new EntityNotFoundException("Floor not found: " + spot.getId()));
        Integer floorNumber = floor.getFloorNumber();
        
        return new ParkingReceipt(
            transaction.getId(),
            vehicle.getLicensePlate(),
            vehicle.getVehicleType(),
            transaction.getCheckInTime(),
            transaction.getCheckOutTime(),
            durationMinutes,
            transaction.getTotalAmount(),
            spot.getSpotNumber(),
            floorNumber
        );
    }
    
    // Helper method to generate receipt breakdown
    public ReceiptBreakdown generateReceiptBreakdown(ParkingTransaction transaction, Vehicle vehicle) {
        PricingConfig pricing = pricingRepository.findByVehicleType(vehicle.getVehicleType())
            .orElseThrow(() -> new RuntimeException("Pricing not found"));
            
        Duration duration = Duration.between(transaction.getCheckInTime(), transaction.getCheckOutTime());
        long peakHours = calculatePeakHours(transaction.getCheckInTime(), transaction.getCheckOutTime(), pricing);
        long weekendDays = calculateWeekendDays(transaction.getCheckInTime(), transaction.getCheckOutTime());
        
        return new ReceiptBreakdown(
            pricing.getBaseRate(),
            pricing.getHourlyRate().multiply(BigDecimal.valueOf(duration.toHours())),
            pricing.getPeakHourSurcharge().multiply(BigDecimal.valueOf(peakHours)),
            pricing.getWeekendSurcharge().multiply(BigDecimal.valueOf(weekendDays)),
            transaction.getTotalAmount()
        );
    }
}