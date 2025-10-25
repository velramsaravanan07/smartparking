package com.project.smartparking.service;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.smartparking.model.*;
import com.project.smartparking.repository.FloorRepository;
import com.project.smartparking.repository.ParkingSpotRepository;

@Service
@Transactional
public class SpotAllocationService {
    
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    
    @Autowired
    private FloorRepository floorRepository;
    
    public Optional<ParkingSpot> allocateSpot(VehicleType vehicleType, Long preferredFloorId) {
        // Try preferred floor first
        if (preferredFloorId != null) {
            List<ParkingSpot> spots = parkingSpotRepository.findAvailableSpotsByFloorAndTypeWithLock(preferredFloorId, vehicleType);
            if (!spots.isEmpty()) {
                return Optional.of(spots.get(0));
            }
        }
        
        // If no exact match on preferred floor, try other floors
        List<ParkingSpot> availableSpots = parkingSpotRepository.findAvailableSpotsByVehicleTypeWithLock(vehicleType);
        if (!availableSpots.isEmpty()) {
            return Optional.of(availableSpots.get(0));
        }
        
        // If no exact match, try larger spots (upgrade)
        return findLargerSpot(vehicleType);
    }
    
    private Optional<ParkingSpot> findLargerSpot(VehicleType vehicleType) {
        List<VehicleType> largerTypes = getLargerVehicleTypes(vehicleType);
        
        for (VehicleType largerType : largerTypes) {
            List<ParkingSpot> availableSpots = parkingSpotRepository.findAvailableSpotsByVehicleTypeWithLock(largerType);
            if (!availableSpots.isEmpty()) {
                return Optional.of(availableSpots.get(0));
            }
        }
        
        return Optional.empty();
    }
    
    private List<VehicleType> getLargerVehicleTypes(VehicleType vehicleType) {
        return switch (vehicleType) {
            case MOTORCYCLE -> Arrays.asList(VehicleType.CAR, VehicleType.SUV, VehicleType.VAN, VehicleType.MINIBUS, VehicleType.BUS, VehicleType.TRUCK);
            case CAR -> Arrays.asList(VehicleType.SUV, VehicleType.VAN, VehicleType.MINIBUS, VehicleType.BUS, VehicleType.TRUCK);
            case SUV -> Arrays.asList(VehicleType.VAN, VehicleType.MINIBUS, VehicleType.BUS, VehicleType.TRUCK);
            case VAN -> Arrays.asList(VehicleType.MINIBUS, VehicleType.BUS, VehicleType.TRUCK);
            case MINIBUS -> Arrays.asList(VehicleType.BUS, VehicleType.TRUCK);
            case BUS -> Arrays.asList(VehicleType.TRUCK);
            default -> new ArrayList<>();
        };
    }
    
    public List<ParkingSpot> getAvailableSpotsByFloorAndType(Long floorId, VehicleType vehicleType) {
        return parkingSpotRepository.findAvailableSpotsByFloorAndTypeWithLock(floorId, vehicleType);
    }
    
    public List<ParkingSpot> getAvailableSpotsByVehicleType(VehicleType vehicleType) {
        return parkingSpotRepository.findAvailableSpotsByVehicleTypeWithLock(vehicleType);
    }
}