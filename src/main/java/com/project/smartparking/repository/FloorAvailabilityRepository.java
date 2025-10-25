package com.project.smartparking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.smartparking.model.FloorAvailability;
import com.project.smartparking.model.VehicleType;

import java.util.List;
import java.util.Optional;

@Repository
public interface FloorAvailabilityRepository extends JpaRepository<FloorAvailability, Long> {
    
    Optional<FloorAvailability> findByFloorIdAndVehicleType(Long floorId, VehicleType vehicleType);
    
    List<FloorAvailability> findByFloorId(Long floorId);
    
    @Modifying
    @Query("UPDATE FloorAvailability fa SET fa.availableSpots = :availableSpots, fa.lastUpdated = CURRENT_TIMESTAMP " +
           "WHERE fa.floor.id = :floorId AND fa.vehicleType = :vehicleType")
    void updateAvailability(@Param("floorId") Long floorId, 
                           @Param("vehicleType") VehicleType vehicleType, 
                           @Param("availableSpots") Integer availableSpots);
    
    @Query("SELECT SUM(fa.availableSpots) FROM FloorAvailability fa WHERE fa.vehicleType = :vehicleType")
    Integer getTotalAvailableSpotsByVehicleType(@Param("vehicleType") VehicleType vehicleType);
    
    @Query("SELECT SUM(fa.totalSpots) FROM FloorAvailability fa WHERE fa.vehicleType = :vehicleType")
    Integer getTotalCapacityByVehicleType(@Param("vehicleType") VehicleType vehicleType);
}