package com.project.smartparking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.smartparking.model.Gate;
import com.project.smartparking.model.GateType;

import java.util.List;
import java.util.Optional;

@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {
    
    List<Gate> findByFloorIdAndGateTypeAndIsOperationalTrue(Long floorId, GateType gateType);
    
    List<Gate> findByIsOperationalTrue();
    
    Optional<Gate> findByGateNumber(String gateNumber);
    
    @Query("SELECT g FROM Gate g WHERE g.floor.id = :floorId AND g.gateType = :gateType AND g.isOperational = true")
    List<Gate> findOperationalGatesByFloorAndType(@Param("floorId") Long floorId, @Param("gateType") GateType gateType);
    
    @Query("SELECT COUNT(g) > 0 FROM Gate g WHERE g.id = :gateId AND g.isOperational = true")
    boolean isGateOperational(@Param("gateId") Long gateId);
}