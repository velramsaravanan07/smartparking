package com.project.smartparking.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.smartparking.model.Floor;
import com.project.smartparking.model.FloorAvailability;
import com.project.smartparking.model.VehicleType;
import com.project.smartparking.repository.FloorAvailabilityRepository;
import com.project.smartparking.repository.FloorRepository;
import com.project.smartparking.repository.ParkingSpotRepository;

@Service
public class FloorAvailabilityService {
    
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    
    @Autowired
    private FloorRepository floorRepository;
    
    @Autowired
    private FloorAvailabilityRepository floorAvailabilityRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Scheduled(fixedRate = 30000) // Update every 30 seconds
    @Transactional
    public void updateRealTimeAvailability() {
        List<Floor> floors = floorRepository.findAll();
        
        for (Floor floor : floors) {
            int totalOccupancy = 0;
            
            for (VehicleType vehicleType : VehicleType.values()) {
                int availableSpots = (int) parkingSpotRepository.countAvailableSpotsByFloorAndType(
                    floor.getId(), vehicleType
                );
                
                int totalSpots = getTotalSpotsByFloorAndType(floor.getId(), vehicleType);
                
                totalOccupancy += (totalSpots - availableSpots);
                
                // Upsert floor availability
                Optional<FloorAvailability> existing = floorAvailabilityRepository
                    .findByFloorIdAndVehicleType(floor.getId(), vehicleType);
                
                if (existing.isPresent()) {
                    FloorAvailability fa = existing.get();
                    fa.setAvailableSpots(availableSpots);
                    floorAvailabilityRepository.save(fa);
                } else {
                    FloorAvailability fa = new FloorAvailability();
                    fa.setFloor(floor);
                    fa.setVehicleType(vehicleType);
                    fa.setAvailableSpots(availableSpots);
                    fa.setTotalSpots(totalSpots);
                    floorAvailabilityRepository.save(fa);
                }
            }
            
            // Update floor occupancy
            floorRepository.updateFloorOccupancy(floor.getId(), totalOccupancy);
        }
    }
    
    private int getTotalSpotsByFloorAndType(Long floorId, VehicleType vehicleType) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM parking_spots WHERE floor_id = ? AND spot_type = ?",
            new Object[]{floorId, vehicleType.name()},
            Integer.class
        );
        return count != null ? count : 0;
    }
    
    public Map<Long, Map<VehicleType, Integer>> getRealTimeAvailability() {
        Map<Long, Map<VehicleType, Integer>> availabilityMap = new HashMap<>();
        List<Floor> floors = floorRepository.findAll();
        
        for (Floor floor : floors) {
            List<FloorAvailability> floorAvailabilities = floorAvailabilityRepository.findByFloorId(floor.getId());
            Map<VehicleType, Integer> floorAvailability = new HashMap<>();
            for (FloorAvailability fa : floorAvailabilities) {
                floorAvailability.put(fa.getVehicleType(), fa.getAvailableSpots());
            }
            availabilityMap.put(floor.getId(), floorAvailability);
        }
        
        return availabilityMap;
    }
}