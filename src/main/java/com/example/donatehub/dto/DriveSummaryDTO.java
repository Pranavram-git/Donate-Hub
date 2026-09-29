package com.example.donatehub.dto;

/**
 * DTO for returning drive summary statistics.
 * Shows total items collected and distributed per drive.
 */
public class DriveSummaryDTO {

    private Long driveId;
    private String driveName;
    private int totalCollected;
    private int totalDistributed;
    private int remainingInStock;

    // Constructors
    public DriveSummaryDTO() {
    }

    public DriveSummaryDTO(Long driveId, String driveName, int totalCollected,
                           int totalDistributed, int remainingInStock) {
        this.driveId = driveId;
        this.driveName = driveName;
        this.totalCollected = totalCollected;
        this.totalDistributed = totalDistributed;
        this.remainingInStock = remainingInStock;
    }

    // Getters and Setters
    public Long getDriveId() {
        return driveId;
    }

    public void setDriveId(Long driveId) {
        this.driveId = driveId;
    }

    public String getDriveName() {
        return driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

    public int getTotalCollected() {
        return totalCollected;
    }

    public void setTotalCollected(int totalCollected) {
        this.totalCollected = totalCollected;
    }

    public int getTotalDistributed() {
        return totalDistributed;
    }

    public void setTotalDistributed(int totalDistributed) {
        this.totalDistributed = totalDistributed;
    }

    public int getRemainingInStock() {
        return remainingInStock;
    }

    public void setRemainingInStock(int remainingInStock) {
        this.remainingInStock = remainingInStock;
    }
}
