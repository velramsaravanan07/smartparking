package com.project.smartparking.repository;

import java.util.*;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.smartparking.model.*;
import com.project.smartparking.repository.FloorRepository;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {
    
    List<Floor> findByIsActiveTrueOrderByFloorNumber();
    
    Optional<Floor> findByFloorNumber(Integer floorNumber);
    
    @Query("SELECT f FROM Floor f WHERE f.floorNumber = :floorNumber AND f.isActive = true")
    Optional<Floor> findActiveByFloorNumber(@Param("floorNumber") Integer floorNumber);
    
    @Modifying
    @Query("UPDATE Floor f SET f.currentOccupancy = :occupancy WHERE f.id = :floorId")
    void updateFloorOccupancy(@Param("floorId") Long floorId, @Param("occupancy") Integer occupancy);
    
    @Query("SELECT COUNT(f) > 0 FROM Floor f WHERE f.floorNumber = :floorNumber AND f.isActive = true")
    boolean existsByFloorNumberAndActive(@Param("floorNumber") Integer floorNumber);
}