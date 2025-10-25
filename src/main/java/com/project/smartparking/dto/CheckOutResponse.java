package com.project.smartparking.dto;

import java.math.BigDecimal;

import com.project.smartparking.model.ParkingReceipt;

public class CheckOutResponse {

	  private String message;
	    private Long transactionId;
	    private BigDecimal totalAmount;
	    private long durationMinutes;
	    private ParkingReceipt receipt;
	    
	    public CheckOutResponse(String message, Long transactionId, BigDecimal totalAmount, 
	                          long durationMinutes, ParkingReceipt receipt) {
	        this.message = message;
	        this.transactionId = transactionId;
	        this.totalAmount = totalAmount;
	        this.durationMinutes = durationMinutes;
	        this.receipt = receipt;
	    }
	    
	    // getters
	    public String getMessage() { return message; }
	    public Long getTransactionId() { return transactionId; }
	    public BigDecimal getTotalAmount() { return totalAmount; }
	    public long getDurationMinutes() { return durationMinutes; }
	    public ParkingReceipt getReceipt() { return receipt; }
	
}
