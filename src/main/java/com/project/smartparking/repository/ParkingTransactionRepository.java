package com.project.smartparking.repository;


import java.util.Optional;


import org.springframework.stereotype.Repository;

import com.project.smartparking.model.ParkingTransaction;
import com.project.smartparking.model.TransactionStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingTransactionRepository extends JpaRepository<ParkingTransaction, Long> {
    
    Optional<ParkingTransaction> findByVehicleIdAndStatus(Long vehicleId, TransactionStatus status);
    
    @Query("SELECT pt FROM ParkingTransaction pt WHERE pt.vehicle.licensePlate = :licensePlate AND pt.status = 'ACTIVE'")
    Optional<ParkingTransaction> findActiveTransactionByLicensePlate(@Param("licensePlate") String licensePlate);
    
    List<ParkingTransaction> findByStatus(TransactionStatus status);
    
    @Query("SELECT pt FROM ParkingTransaction pt WHERE pt.checkInTime >= :startDate AND pt.checkInTime < :endDate")
    List<ParkingTransaction> findTransactionsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(pt) FROM ParkingTransaction pt WHERE pt.status = 'ACTIVE'")
    long countActiveTransactions();
    
    @Query("SELECT pt FROM ParkingTransaction pt WHERE pt.vehicle.id = :vehicleId ORDER BY pt.checkInTime DESC")
    List<ParkingTransaction> findVehicleHistory(@Param("vehicleId") Long vehicleId);
    
    @Query("SELECT SUM(pt.totalAmount) FROM ParkingTransaction pt WHERE pt.checkOutTime BETWEEN :startDate AND :endDate")
    Optional<Double> calculateRevenueBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);
}