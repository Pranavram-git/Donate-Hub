package com.example.donatehub.service;

import com.example.donatehub.dto.DriveDTO;
import com.example.donatehub.dto.DriveSummaryDTO;
import com.example.donatehub.exception.BusinessRuleException;
import com.example.donatehub.exception.ResourceNotFoundException;
import com.example.donatehub.entity.DonatedItem;
import com.example.donatehub.entity.Drive;
import com.example.donatehub.repository.DonatedItemRepository;
import com.example.donatehub.repository.DriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriveService {

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private DonatedItemRepository donatedItemRepository;

    /**
     * Create a new donation drive with start and end dates.
     * Validates that end date is not before start date.
     */
    public Drive createDrive(DriveDTO driveDTO) {
        // Business rule: end date must not be before start date
        if (driveDTO.getEndDate().isBefore(driveDTO.getStartDate())) {
            throw new BusinessRuleException("End date cannot be before start date.");
        }

        Drive drive = new Drive();
        drive.setName(driveDTO.getName());
        drive.setStartDate(driveDTO.getStartDate());
        drive.setEndDate(driveDTO.getEndDate());

        return driveRepository.save(drive);
    }

    /**
     * Get all drives.
     */
    public List<Drive> getAllDrives() {
        return driveRepository.findAll();
    }

    /**
     * Get a single drive by ID.
     */
    public Drive getDriveById(Long id) {
        return driveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Drive not found with ID: " + id));
    }

    /**
     * Update an existing drive.
     */
    public Drive updateDrive(Long id, DriveDTO driveDTO) {
        Drive drive = getDriveById(id);

        if (driveDTO.getEndDate().isBefore(driveDTO.getStartDate())) {
            throw new BusinessRuleException("End date cannot be before start date.");
        }

        drive.setName(driveDTO.getName());
        drive.setStartDate(driveDTO.getStartDate());
        drive.setEndDate(driveDTO.getEndDate());

        return driveRepository.save(drive);
    }

    /**
     * Delete a drive by ID.
     */
    public void deleteDrive(Long id) {
        Drive drive = getDriveById(id);
        driveRepository.delete(drive);
    }

    /**
     * Feature #4: View total items collected and distributed per drive.
     * Returns a summary with counts.
     */
    public DriveSummaryDTO getDriveSummary(Long driveId) {
        Drive drive = getDriveById(driveId);

        List<DonatedItem> allItems = donatedItemRepository.findByDriveId(driveId);
        List<DonatedItem> distributedItems =
                donatedItemRepository.findByDriveIdAndDistributedTrue(driveId);

        int totalCollected = allItems.size();
        int totalDistributed = distributedItems.size();
        int remainingInStock = totalCollected - totalDistributed;

        return new DriveSummaryDTO(
                drive.getId(),
                drive.getName(),
                totalCollected,
                totalDistributed,
                remainingInStock
        );
    }
}
