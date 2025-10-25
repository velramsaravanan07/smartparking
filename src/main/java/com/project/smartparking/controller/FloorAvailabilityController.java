package com.project.smartparking.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.smartparking.model.VehicleType;
import com.project.smartparking.service.FloorAvailabilityService;

@RestController
@RequestMapping("/api/floors")
public class FloorAvailabilityController {

    @Autowired
    private FloorAvailabilityService floorAvailabilityService;

    @GetMapping("/availability")
    public ResponseEntity<Map<Long, Map<VehicleType, Integer>>> getRealTimeAvailability() {
        Map<Long, Map<VehicleType, Integer>> availability = floorAvailabilityService.getRealTimeAvailability();
        return ResponseEntity.ok(availability);
    }
}