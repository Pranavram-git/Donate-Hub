package com.example.donatehub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Recipient organization that receives distributed items.
 */
@Entity
@Table(name = "recipients")
public class Recipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Recipient organization name is required")
    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    private String address;

    private String contactPerson;

    // One Recipient can receive many items
    @JsonIgnore
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DonatedItem> receivedItems = new ArrayList<>();

    // Constructors
    public Recipient() {
    }

    public Recipient(String organizationName, String address, String contactPerson) {
        this.organizationName = organizationName;
        this.address = address;
        this.contactPerson = contactPerson;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<DonatedItem> getReceivedItems() {
        return receivedItems;
    }

    public void setReceivedItems(List<DonatedItem> receivedItems) {
        this.receivedItems = receivedItems;
    }
}
