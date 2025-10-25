package com.project.smartparking.dto;

public class CheckInResponse {
	  private String message;
	    private Long transactionId;
	    private Long spotId;
	    private Long floorId;
	    private String spotNumber;
	    
	    public CheckInResponse(String message, Long transactionId, Long spotId, Long floorId, String spotNumber) {
	        this.message = message;
	        this.transactionId = transactionId;
	        this.spotId = spotId;
	        this.floorId = floorId;
	        this.spotNumber = spotNumber;
	    }
	    
	    // getters
	    public String getMessage() { return message; }
	    public Long getTransactionId() { return transactionId; }
	    public Long getSpotId() { return spotId; }
	    public Long getFloorId() { return floorId; }
	    public String getSpotNumber() { return spotNumber; }
}