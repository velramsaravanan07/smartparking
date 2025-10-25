package com.project.smartparking.service;

import com.project.smartparking.model.*;
import com.project.smartparking.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class ParkingService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    
    @Autowired
    private ParkingTransactionRepository parkingTransactionRepository;
    
    @Autowired
    private FloorRepository floorRepository;
    
    @Autowired
    private GateRepository gateRepository;
    
    @Autowired
    private FloorAvailabilityRepository floorAvailabilityRepository;
    
    @Autowired
    private SpotAllocationService spotAllocationService;
    
    @Autowired
    private FeeCalculationService feeCalculationService;
    
    @Autowired
    private FloorAvailabilityService floorAvailabilityService;
    
    private final Map<String, Object> vehicleLocks = new ConcurrentHashMap<>();
    
    private Object getVehicleLock(String licensePlate) {
        return vehicleLocks.computeIfAbsent(licensePlate.toUpperCase(), k -> new Object());
    }
    
    public ParkingTransaction checkIn(String licensePlate, VehicleType vehicleType, Long entryGateId) {
        String lockKey = licensePlate.toUpperCase();
        synchronized (getVehicleLock(lockKey)) {
            // Validate gate
            Gate entryGate = gateRepository.findById(entryGateId)
                .orElseThrow(() -> new EntityNotFoundException("Gate not found: " + entryGateId));
            
            if (!entryGate.getIsOperational()) {
                throw new IllegalStateException("Gate is not operational: " + entryGate.getGateNumber());
            }
            
            if (entryGate.getGateType() != GateType.ENTRY) {
                throw new IllegalStateException("Invalid gate type. Expected ENTRY gate");
            }
            
            // Check if vehicle is already parked
            if (vehicleRepository.isVehicleCurrentlyParked(licensePlate)) {
                throw new IllegalStateException("Vehicle is already parked: " + licensePlate);
            }
            
            // Allocate parking spot with floor preference
            ParkingSpot spot = spotAllocationService.allocateSpot(vehicleType, entryGate.getFloor().getId())
                .orElseThrow(() -> new IllegalStateException("No available spots for vehicle type: " + vehicleType));
            
            // Create or update vehicle
            Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElse(new Vehicle(licensePlate, vehicleType));
            vehicle.setCheckInTime(LocalDateTime.now());
            vehicle.setCheckOutTime(null);
            vehicle = vehicleRepository.save(vehicle);
            
            // Update spot availability
            spot.setIsAvailable(false);
            spot.setStatus(SpotStatus.OCCUPIED);
            parkingSpotRepository.save(spot);
            
            // Create transaction
            ParkingTransaction transaction = new ParkingTransaction();
            transaction.setVehicle(vehicle);
            transaction.setParkingSpot(spot);
            transaction.setEntryGate(entryGate);
            transaction.setCheckInTime(LocalDateTime.now());
            transaction.setStatus(TransactionStatus.ACTIVE);
            
            ParkingTransaction savedTransaction = parkingTransactionRepository.save(transaction);
            
            // Update floor occupancy
            updateFloorOccupancy(spot.getFloor().getId());
            
            // Update real-time availability
            floorAvailabilityService.updateRealTimeAvailability();
            
            return savedTransaction;
        }
    }
    
    public ParkingTransaction checkOut(String licensePlate, Long exitGateId) {
        String lockKey = licensePlate.toUpperCase();
        synchronized (getVehicleLock(lockKey)) {
            // Validate gate
            Gate exitGate = gateRepository.findById(exitGateId)
                .orElseThrow(() -> new EntityNotFoundException("Gate not found: " + exitGateId));
            
            if (!exitGate.getIsOperational()) {
                throw new IllegalStateException("Gate is not operational: " + exitGate.getGateNumber());
            }
            
            if (exitGate.getGateType() != GateType.EXIT) {
                throw new IllegalStateException("Invalid gate type. Expected EXIT gate");
            }
            
            // Find active vehicle
            Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found: " + licensePlate));
                
            if (vehicle.getCheckOutTime() != null) {
                throw new IllegalStateException("Vehicle already checked out: " + licensePlate);
            }
            
            // Find active transaction
            ParkingTransaction transaction = parkingTransactionRepository
                .findActiveTransactionByLicensePlate(licensePlate)
                .orElseThrow(() -> new IllegalStateException("No active transaction found for: " + licensePlate));
            
            // Update check-out time
            LocalDateTime checkOutTime = LocalDateTime.now();
            vehicle.setCheckOutTime(checkOutTime);
            vehicleRepository.save(vehicle);
            
            // Calculate fee
            BigDecimal totalAmount = feeCalculationService.calculateFee(
                vehicle.getVehicleType(), 
                transaction.getCheckInTime(), 
                checkOutTime
            );
            
            long durationMinutes = java.time.Duration.between(transaction.getCheckInTime(), checkOutTime).toMinutes();
            
            // Update transaction
            transaction.setCheckOutTime(checkOutTime);
            transaction.setTotalAmount(totalAmount);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setDurationMinutes((int) durationMinutes);
            transaction.setExitGate(exitGate);
            ParkingTransaction updatedTransaction = parkingTransactionRepository.save(transaction);
            
            // Free up parking spot
            ParkingSpot spot = transaction.getParkingSpot();
            spot.setIsAvailable(true);
            spot.setStatus(SpotStatus.AVAILABLE);
            parkingSpotRepository.save(spot);
            
            // Update floor occupancy
            updateFloorOccupancy(spot.getFloor().getId());
            
            // Update real-time availability
            floorAvailabilityService.updateRealTimeAvailability();
            
            // Remove vehicle lock
            vehicleLocks.remove(lockKey);
            
            return updatedTransaction;
        }
    }
    
    private void updateFloorOccupancy(Long floorId) {
        Floor floor = floorRepository.findById(floorId)
            .orElseThrow(() -> new EntityNotFoundException("Floor not found: " + floorId));
        
        long occupiedSpots = parkingSpotRepository.countByFloorIdAndIsAvailableFalse(floorId);
        floor.setCurrentOccupancy((int) occupiedSpots);
        floorRepository.save(floor);
    }
    
    public Map<Long, Map<VehicleType, Integer>> getRealTimeAvailability() {
        return floorAvailabilityService.getRealTimeAvailability();
    }
    
    public List<ParkingTransaction> getVehicleHistory(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found: " + licensePlate));
        
        return parkingTransactionRepository.findVehicleHistory(vehicle.getId());
    }
    
    public long getTotalParkedVehicles() {
        return vehicleRepository.countCurrentlyParkedVehicles();
    }
    
    public Optional<Double> getDailyRevenue(LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        return parkingTransactionRepository.calculateRevenueBetweenDates(startOfDay, endOfDay);
    }
}

// Add this method to ParkingSpotRepository
// long countByFloorIdAndIsAvailableFalse(Long floorId);