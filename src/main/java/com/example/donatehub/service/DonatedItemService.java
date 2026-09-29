package com.example.donatehub.service;

import com.example.donatehub.dto.DonatedItemDTO;
import com.example.donatehub.dto.DistributionDTO;
import com.example.donatehub.exception.BusinessRuleException;
import com.example.donatehub.exception.ResourceNotFoundException;
import com.example.donatehub.entity.*;
import com.example.donatehub.repository.DonatedItemRepository;
import com.example.donatehub.repository.DonorRepository;
import com.example.donatehub.repository.DriveRepository;
import com.example.donatehub.repository.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonatedItemService {

    @Autowired
    private DonatedItemRepository donatedItemRepository;

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private RecipientRepository recipientRepository;

    /**
     * Feature #2: Log a donated item with category, condition and donor name.
     * Business Rule: Item condition must be one of a fixed set (NEW, GOOD, WORN).
     * Enforced in the service layer — invalid condition is rejected immediately.
     */
    public DonatedItem logDonatedItem(DonatedItemDTO dto) {
        // Validate item condition in service layer (not just DB)
        ItemCondition condition;
        try {
            condition = ItemCondition.valueOf(dto.getItemCondition().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException(
                    "Invalid item condition: '" + dto.getItemCondition()
                            + "'. Must be one of: NEW, GOOD, WORN.");
        }

        // Check that the drive exists
        Drive drive = driveRepository.findById(dto.getDriveId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Drive not found with ID: " + dto.getDriveId()));

        // Check that the donor exists
        Donor donor = donorRepository.findById(dto.getDonorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Donor not found with ID: " + dto.getDonorId()));

        // Create and save the donated item
        DonatedItem item = new DonatedItem();
        item.setDescription(dto.getDescription());
        item.setCategory(dto.getCategory());
        item.setItemCondition(condition);
        item.setDrive(drive);
        item.setDonor(donor);
        item.setDistributed(false); // initially not distributed

        return donatedItemRepository.save(item);
    }

    /**
     * Feature #3: Record distribution of items to a recipient organization.
     * Business Rule: An item can be distributed ONLY ONCE it is logged as collected.
     * An already-distributed item cannot be distributed again.
     */
    public DonatedItem distributeItem(DistributionDTO dto) {
        // Find the item
        DonatedItem item = donatedItemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Donated item not found with ID: " + dto.getItemId()));

        // Business Rule: item must not already be distributed
        if (item.isDistributed()) {
            throw new BusinessRuleException(
                    "Item with ID " + dto.getItemId()
                            + " has already been distributed. An item can only be distributed once.");
        }

        // Find the recipient
        Recipient recipient = recipientRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recipient not found with ID: " + dto.getRecipientId()));

        // Mark as distributed and assign recipient
        item.setDistributed(true);
        item.setRecipient(recipient);

        return donatedItemRepository.save(item);
    }

    /**
     * Update an existing donated item.
     */
    public DonatedItem updateItem(Long id, DonatedItemDTO dto) {
        DonatedItem item = getItemById(id);
        
        ItemCondition condition;
        try {
            condition = ItemCondition.valueOf(dto.getItemCondition().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException(
                    "Invalid item condition: '" + dto.getItemCondition()
                            + "'. Must be one of: NEW, GOOD, WORN.");
        }

        item.setDescription(dto.getDescription());
        item.setCategory(dto.getCategory());
        item.setItemCondition(condition);
        
        // Update relationships if needed
        Drive drive = driveRepository.findById(dto.getDriveId())
                .orElseThrow(() -> new ResourceNotFoundException("Drive not found"));
        Donor donor = donorRepository.findById(dto.getDonorId())
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found"));
                
        item.setDrive(drive);
        item.setDonor(donor);

        return donatedItemRepository.save(item);
    }

    /**
     * Feature #5: List undistributed items remaining in stock.
     */
    public List<DonatedItem> getUndistributedItems() {
        return donatedItemRepository.findByDistributedFalse();
    }

    /**
     * Get all donated items for a specific drive.
     */
    public List<DonatedItem> getItemsByDrive(Long driveId) {
        // Verify drive exists
        driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Drive not found with ID: " + driveId));

        return donatedItemRepository.findByDriveId(driveId);
    }

    /**
     * Get all donated items.
     */
    public List<DonatedItem> getAllItems() {
        return donatedItemRepository.findAll();
    }

    /**
     * Get a single donated item by ID.
     */
    public DonatedItem getItemById(Long id) {
        return donatedItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Donated item not found with ID: " + id));
    }
}
