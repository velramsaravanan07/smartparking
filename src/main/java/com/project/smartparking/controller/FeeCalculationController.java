package com.project.smartparking.controller;



import com.project.smartparking.model.VehicleType;
import com.project.smartparking.repository.ParkingTransactionRepository;
import com.project.smartparking.service.FeeCalculationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/fee")
public class FeeCalculationController {

    @Autowired
    private FeeCalculationService feeCalculationService;

    @Autowired
    private ParkingTransactionRepository parkingTransactionRepository;

   

    @PostMapping("/calculate")
    public ResponseEntity<BigDecimal> calculateFee(
            @RequestParam VehicleType vehicleType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOut) {
        BigDecimal fee = feeCalculationService.calculateFee(vehicleType, checkIn, checkOut);
        return ResponseEntity.ok(fee);
    }

 
  
}