package com.example.donatehub.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for distributing an item to a recipient organization.
 */
public class DistributionDTO {

    @NotNull(message = "Item ID is required")
    @Positive(message = "Item ID must be a positive number")
    private Long itemId;

    @NotNull(message = "Recipient ID is required")
    @Positive(message = "Recipient ID must be a positive number")
    private Long recipientId;

    // Constructors
    public DistributionDTO() {
    }

    public DistributionDTO(Long itemId, Long recipientId) {
        this.itemId = itemId;
        this.recipientId = recipientId;
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
}
