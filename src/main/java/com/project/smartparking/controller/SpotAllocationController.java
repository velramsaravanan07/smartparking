package com.project.smartparking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.smartparking.model.ParkingSpot;
import com.project.smartparking.model.VehicleType;
import com.project.smartparking.service.SpotAllocationService;

@RestController
@RequestMapping("/api/spots")
public class SpotAllocationController {

    @Autowired
    private SpotAllocationService spotAllocationService;

    @GetMapping("/available/floor/{floorId}")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpotsByFloorAndType(
            @PathVariable Long floorId,
            @RequestParam VehicleType vehicleType) {
        List<ParkingSpot> spots = spotAllocationService.getAvailableSpotsByFloorAndType(floorId, vehicleType);
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/available/type")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpotsByVehicleType(
            @RequestParam VehicleType vehicleType) {
        List<ParkingSpot> spots = spotAllocationService.getAvailableSpotsByVehicleType(vehicleType);
        return ResponseEntity.ok(spots);
    }
}