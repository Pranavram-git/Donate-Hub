package com.example.donatehub.service;

import com.example.donatehub.dto.RecipientDTO;
import com.example.donatehub.exception.ResourceNotFoundException;
import com.example.donatehub.entity.Recipient;
import com.example.donatehub.repository.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipientService {

    @Autowired
    private RecipientRepository recipientRepository;

    /**
     * Create a new recipient organization.
     */
    public Recipient createRecipient(RecipientDTO recipientDTO) {
        Recipient recipient = new Recipient();
        recipient.setOrganizationName(recipientDTO.getOrganizationName());
        recipient.setAddress(recipientDTO.getAddress());
        recipient.setContactPerson(recipientDTO.getContactPerson());
        return recipientRepository.save(recipient);
    }

    /**
     * Get all recipients.
     */
    public List<Recipient> getAllRecipients() {
        return recipientRepository.findAll();
    }

    /**
     * Get a single recipient by ID.
     */
    public Recipient getRecipientById(Long id) {
        return recipientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recipient not found with ID: " + id));
    }

    /**
     * Update an existing recipient.
     */
    public Recipient updateRecipient(Long id, RecipientDTO recipientDTO) {
        Recipient recipient = getRecipientById(id);
        recipient.setOrganizationName(recipientDTO.getOrganizationName());
        recipient.setAddress(recipientDTO.getAddress());
        recipient.setContactPerson(recipientDTO.getContactPerson());
        return recipientRepository.save(recipient);
    }

    /**
     * Delete a recipient by ID.
     */
    public void deleteRecipient(Long id) {
        Recipient recipient = getRecipientById(id);
        recipientRepository.delete(recipient);
    }
}
