package com.example.donatehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entity representing a Donated Item.
 * Each item belongs to a Drive and a Donor.
 * It may optionally be distributed to a Recipient.
 * Business Rules:
 * - condition must be NEW, GOOD, or WORN
 * - item can only be distributed once it is logged as collected (distributed = true only after creation)
 */
@Entity
@Table(name = "donated_items")
public class DonatedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item description is required")
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "Category is required (e.g., clothes, books)")
    @Column(nullable = false)
    private String category;

    @NotNull(message = "Item condition is required (NEW, GOOD, or WORN)")
    @Enumerated(EnumType.STRING)
    @Column(name = "item_condition", nullable = false)
    private ItemCondition itemCondition;

    // Tracks whether this item has been distributed
    @Column(nullable = false)
    private boolean distributed = false;

    // Many DonatedItems belong to one Drive
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drive_id", nullable = false)
    private Drive drive;

    // Many DonatedItems belong to one Donor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;

    // Many DonatedItems can be distributed to one Recipient (nullable until distributed)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Recipient recipient;

    // Constructors
    public DonatedItem() {
    }

    public DonatedItem(String description, String category, ItemCondition itemCondition,
                       Drive drive, Donor donor) {
        this.description = description;
        this.category = category;
        this.itemCondition = itemCondition;
        this.drive = drive;
        this.donor = donor;
        this.distributed = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public ItemCondition getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(ItemCondition itemCondition) {
        this.itemCondition = itemCondition;
    }

    public boolean isDistributed() {
        return distributed;
    }

    public void setDistributed(boolean distributed) {
        this.distributed = distributed;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }
}
