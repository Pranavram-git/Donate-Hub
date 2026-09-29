package com.example.donatehub.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating/updating a Recipient organization.
 */
public class RecipientDTO {

    @NotBlank(message = "Organization name is required")
    private String organizationName;

    private String address;

    private String contactPerson;

    // Constructors
    public RecipientDTO() {
    }

    public RecipientDTO(String organizationName, String address, String contactPerson) {
        this.organizationName = organizationName;
        this.address = address;
        this.contactPerson = contactPerson;
    }

    // Getters and Setters
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
}
