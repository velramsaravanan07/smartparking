package com.project.smartparking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.smartparking.model.PricingConfig;
import com.project.smartparking.model.VehicleType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PricingConfigRepository extends JpaRepository<PricingConfig, Long> {
    
    @Query("SELECT pc FROM PricingConfig pc WHERE pc.vehicleType = :vehicleType " +
           "AND pc.isActive = true " +
           "AND (pc.effectiveTo IS NULL OR pc.effectiveTo >= CURRENT_DATE) " +
           "ORDER BY pc.effectiveFrom DESC")
    List<PricingConfig> findActiveByVehicleType(@Param("vehicleType") VehicleType vehicleType);
    
    default Optional<PricingConfig> findByVehicleType(VehicleType vehicleType) {
        List<PricingConfig> configs = findActiveByVehicleType(vehicleType);
        return configs.stream().findFirst();
    }
    
    List<PricingConfig> findByVehicleTypeAndIsActiveTrue(VehicleType vehicleType);
    
    @Query("SELECT pc FROM PricingConfig pc WHERE pc.vehicleType = :vehicleType " +
           "AND :date BETWEEN pc.effectiveFrom AND COALESCE(pc.effectiveTo, CURRENT_DATE) " +
           "AND pc.isActive = true")
    Optional<PricingConfig> findByVehicleTypeAndDate(@Param("vehicleType") VehicleType vehicleType,
                                                    @Param("date") LocalDate date);
    
    @Query("SELECT pc FROM PricingConfig pc WHERE pc.isActive = true " +
           "AND (pc.effectiveTo IS NULL OR pc.effectiveTo >= CURRENT_DATE)")
    List<PricingConfig> findAllActive();
}