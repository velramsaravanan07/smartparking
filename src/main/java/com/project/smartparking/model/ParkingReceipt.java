package com.project.smartparking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingReceipt {
    private final Long transactionId;
    private final String licensePlate;
    private final VehicleType vehicleType;
    private final LocalDateTime checkInTime;
    private final LocalDateTime checkOutTime;
    private final long durationMinutes;
    private final long durationHours;
    private final long durationDays;
    private final BigDecimal totalAmount;
    private final String spotNumber;
    private final Integer floorNumber;
    private final LocalDateTime issuedAt;
    private final String receiptNumber;

    public ParkingReceipt(Long transactionId, String licensePlate, VehicleType vehicleType,
                         LocalDateTime checkInTime, LocalDateTime checkOutTime,
                         long durationMinutes, BigDecimal totalAmount,
                         String spotNumber, Integer floorNumber) {
        this.transactionId = transactionId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.durationMinutes = durationMinutes;
        this.durationHours = durationMinutes / 60;
        this.durationDays = durationMinutes / (60 * 24);
        this.totalAmount = totalAmount;
        this.spotNumber = spotNumber;
        this.floorNumber = floorNumber;
        this.issuedAt = LocalDateTime.now();
        this.receiptNumber = generateReceiptNumber();
    }

    // Getters
    public Long getTransactionId() { return transactionId; }
    public String getLicensePlate() { return licensePlate; }
    public VehicleType getVehicleType() { return vehicleType; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public long getDurationMinutes() { return durationMinutes; }
    public long getDurationHours() { return durationHours; }
    public long getDurationDays() { return durationDays; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getSpotNumber() { return spotNumber; }
    public Integer getFloorNumber() { return floorNumber; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public String getReceiptNumber() { return receiptNumber; }

    private String generateReceiptNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = issuedAt.format(formatter);
        return "RCP-" + timestamp + "-" + String.format("%04d", transactionId % 10000);
    }

    public String toFormattedString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        return """
               ╔══════════════════════════════════════╗
               ║           PARKING RECEIPT           ║
               ╠══════════════════════════════════════╣
               ║ Receipt: %-25s ║
               ║ Transaction: %-22s ║
               ╠══════════════════════════════════════╣
               ║ Vehicle: %-26s ║
               ║ License: %-26s ║
               ║ Spot: %-29s ║
               ╠══════════════════════════════════════╣
               ║ Check-in:  %-24s ║
               ║ Check-out: %-24s ║
               ║ Duration:  %-24s ║
               ╠══════════════════════════════════════╣
               ║ Total Amount: $%-21.2f ║
               ╠══════════════════════════════════════╣
               ║         THANK YOU FOR PARKING       ║
               ║         HAVE A SAFE JOURNEY!        ║
               ╚══════════════════════════════════════╝
               """.formatted(
                receiptNumber,
                "TX-" + transactionId,
                vehicleType.toString(),
                licensePlate,
                "Floor " + floorNumber + ", Spot " + spotNumber,
                checkInTime.format(formatter),
                checkOutTime.format(formatter),
                formatDuration(durationMinutes),
                totalAmount
            );
    }

    private String formatDuration(long totalMinutes) {
        long days = totalMinutes / (60 * 24);
        long hours = (totalMinutes % (60 * 24)) / 60;
        long minutes = totalMinutes % 60;

        if (days > 0) {
            return String.format("%d days, %d hrs, %d min", days, hours, minutes);
        } else if (hours > 0) {
            return String.format("%d hours, %d minutes", hours, minutes);
        } else {
            return String.format("%d minutes", minutes);
        }
    }

    public String toJson() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        return """
            {
                "receiptNumber": "%s",
                "transactionId": %d,
                "licensePlate": "%s",
                "vehicleType": "%s",
                "checkInTime": "%s",
                "checkOutTime": "%s",
                "durationMinutes": %d,
                "spotNumber": "%s",
                "floorNumber": %d,
                "totalAmount": %.2f,
                "issuedAt": "%s"
            }
            """.formatted(
                receiptNumber,
                transactionId,
                licensePlate,
                vehicleType.toString(),
                checkInTime.format(formatter),
                checkOutTime.format(formatter),
                durationMinutes,
                spotNumber,
                floorNumber,
                totalAmount,
                issuedAt.format(formatter)
            );
    }

    @Override
    public String toString() {
        return toFormattedString();
    }
}