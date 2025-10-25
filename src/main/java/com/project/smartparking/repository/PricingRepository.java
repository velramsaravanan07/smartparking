package com.project.smartparking.repository;

import com.project.smartparking.model.PricingConfig;
import com.project.smartparking.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PricingRepository extends JpaRepository<PricingConfig, Long> {
    Optional<PricingConfig> findByVehicleType(VehicleType vehicleType);
}