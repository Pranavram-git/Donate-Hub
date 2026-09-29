package com.example.donatehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for logging a new donated item.
 */
public class DonatedItemDTO {

    @NotBlank(message = "Item description is required")
    private String description;

    @NotBlank(message = "Category is required (e.g., clothes, books)")
    private String category;

    @NotBlank(message = "Item condition is required (NEW, GOOD, or WORN)")
    private String itemCondition;

    @NotNull(message = "Drive ID is required")
    @Positive(message = "Drive ID must be a positive number")
    private Long driveId;

    @NotNull(message = "Donor ID is required")
    @Positive(message = "Donor ID must be a positive number")
    private Long donorId;

    // Constructors
    public DonatedItemDTO() {
    }

    public DonatedItemDTO(String description, String category, String itemCondition,
                          Long driveId, Long donorId) {
        this.description = description;
        this.category = category;
        this.itemCondition = itemCondition;
        this.driveId = driveId;
        this.donorId = donorId;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public Long getDriveId() {
        return driveId;
    }

    public void setDriveId(Long driveId) {
        this.driveId = driveId;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }
}
