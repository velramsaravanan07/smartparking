package com.project.smartparking.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.smartparking.model.ParkingTransaction;
import com.project.smartparking.model.VehicleType;
import com.project.smartparking.service.ParkingService;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @PostMapping("/check-in")
    public ResponseEntity<ParkingTransaction> checkIn(
            @RequestParam String licensePlate,
            @RequestParam VehicleType vehicleType,
            @RequestParam Long entryGateId) {
        ParkingTransaction transaction = parkingService.checkIn(licensePlate, vehicleType, entryGateId);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/check-out")
    public ResponseEntity<ParkingTransaction> checkOut(
            @RequestParam String licensePlate,
            @RequestParam Long exitGateId) {
        ParkingTransaction transaction = parkingService.checkOut(licensePlate, exitGateId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/availability")
    public ResponseEntity<Map<Long, Map<VehicleType, Integer>>> getRealTimeAvailability() {
        Map<Long, Map<VehicleType, Integer>> availability = parkingService.getRealTimeAvailability();
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/history/{licensePlate}")
    public ResponseEntity<List<ParkingTransaction>> getVehicleHistory(@PathVariable String licensePlate) {
        List<ParkingTransaction> history = parkingService.getVehicleHistory(licensePlate);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/total-parked")
    public ResponseEntity<Long> getTotalParkedVehicles() {
        long total = parkingService.getTotalParkedVehicles();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Double> getDailyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        Optional<Double> revenue = parkingService.getDailyRevenue(date);
        return revenue.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
}