package com.example.donatehub.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating/updating a Donor.
 */
public class DonorDTO {

    @NotBlank(message = "Donor name is required")
    private String name;

    private String email;

    private String phone;

    // Constructors
    public DonorDTO() {
    }

    public DonorDTO(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
