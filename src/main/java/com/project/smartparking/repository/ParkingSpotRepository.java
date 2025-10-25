package com.project.smartparking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.smartparking.model.ParkingSpot;
import com.project.smartparking.model.SpotStatus;
import com.project.smartparking.model.VehicleType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    
	  List<ParkingSpot> findByFloorIdAndSpotTypeAndIsAvailableTrueAndStatus(Long floorId, VehicleType spotType, SpotStatus status);
	    
	    List<ParkingSpot> findBySpotTypeAndIsAvailableTrueAndStatus(VehicleType spotType, SpotStatus status);
	    
	    @Lock(LockModeType.PESSIMISTIC_WRITE)
	    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.floor.id = :floorId AND ps.spotType = :vehicleType " +
	           "AND ps.isAvailable = true AND ps.status = 'AVAILABLE' " +
	           "ORDER BY ps.proximityToEntry ASC")
	    List<ParkingSpot> findAvailableSpotsByFloorAndTypeWithLock(@Param("floorId") Long floorId, 
	                                                              @Param("vehicleType") VehicleType vehicleType);
	    
	    @Lock(LockModeType.PESSIMISTIC_WRITE)
	    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.spotType = :vehicleType " +
	           "AND ps.isAvailable = true AND ps.status = 'AVAILABLE' " +
	           "ORDER BY ps.floor.floorNumber ASC, ps.proximityToEntry ASC")
	    List<ParkingSpot> findAvailableSpotsByVehicleTypeWithLock(@Param("vehicleType") VehicleType vehicleType);
	    
	    @Query("SELECT COUNT(ps) FROM ParkingSpot ps WHERE ps.floor.id = :floorId AND ps.spotType = :vehicleType " +
	           "AND ps.isAvailable = true AND ps.status = 'AVAILABLE'")
	    long countAvailableSpotsByFloorAndType(@Param("floorId") Long floorId, @Param("vehicleType") VehicleType vehicleType);
	    
	    @Query("SELECT COUNT(ps) FROM ParkingSpot ps WHERE ps.floor.id = :floorId AND ps.spotType = :vehicleType")
	    long countTotalSpotsByFloorAndType(@Param("floorId") Long floorId, @Param("vehicleType") VehicleType vehicleType);
	    
	    @Modifying
	    @Query("UPDATE ParkingSpot ps SET ps.isAvailable = :isAvailable, ps.status = :status WHERE ps.id = :spotId")
	    void updateSpotAvailability(@Param("spotId") Long spotId, 
	                               @Param("isAvailable") Boolean isAvailable, 
	                               @Param("status") SpotStatus status);
	    
	    Optional<ParkingSpot> findBySpotNumberAndFloorId(String spotNumber, Long floorId);
	    
	    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.floor.id = :floorId ORDER BY ps.spotNumber")
	    List<ParkingSpot> findByFloorIdOrderBySpotNumber(@Param("floorId") Long floorId);
	    
	    @Query("SELECT COUNT(ps) FROM ParkingSpot ps WHERE ps.floor.id = :floorId AND ps.isAvailable = false")
	    long countByFloorIdAndIsAvailableFalse(@Param("floorId") Long floorId);
	}