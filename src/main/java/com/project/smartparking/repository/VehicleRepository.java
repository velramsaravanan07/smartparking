package com.project.smartparking.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.smartparking.model.Vehicle;
import com.project.smartparking.model.VehicleType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    
    @Query("SELECT v FROM Vehicle v WHERE v.licensePlate = :licensePlate AND v.checkOutTime IS NULL")
    Optional<Vehicle> findActiveVehicleByLicensePlate(@Param("licensePlate") String licensePlate);
    
    @Query("SELECT COUNT(v) > 0 FROM Vehicle v WHERE v.licensePlate = :licensePlate AND v.checkOutTime IS NULL")
    boolean isVehicleCurrentlyParked(@Param("licensePlate") String licensePlate);
    
    List<Vehicle> findByCheckOutTimeIsNull();
    
    @Query("SELECT v FROM Vehicle v WHERE v.checkInTime IS NOT NULL AND v.checkOutTime IS NULL")
    List<Vehicle> findAllCurrentlyParked();
    
    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.checkOutTime IS NULL")
    long countCurrentlyParkedVehicles();
}