package com.example.donatehub.controller;

import com.example.donatehub.dto.RecipientDTO;
import com.example.donatehub.entity.Recipient;
import com.example.donatehub.service.RecipientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Recipient operations.
 */
@RestController
@RequestMapping("/api/recipients")
public class RecipientController {

    @Autowired
    private RecipientService recipientService;

    // Create a new recipient organization
    @PostMapping
    public ResponseEntity<Recipient> createRecipient(
            @Valid @RequestBody RecipientDTO recipientDTO) {
        Recipient createdRecipient = recipientService.createRecipient(recipientDTO);
        return new ResponseEntity<>(createdRecipient, HttpStatus.CREATED);
    }

    // Get all recipients
    @GetMapping
    public ResponseEntity<List<Recipient>> getAllRecipients() {
        List<Recipient> recipients = recipientService.getAllRecipients();
        return ResponseEntity.ok(recipients);
    }

    // Get a single recipient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipient> getRecipientById(@PathVariable Long id) {
        Recipient recipient = recipientService.getRecipientById(id);
        return ResponseEntity.ok(recipient);
    }

    // Update a recipient
    @PutMapping("/{id}")
    public ResponseEntity<Recipient> updateRecipient(@PathVariable Long id,
                                                     @Valid @RequestBody RecipientDTO recipientDTO) {
        Recipient updatedRecipient = recipientService.updateRecipient(id, recipientDTO);
        return ResponseEntity.ok(updatedRecipient);
    }

    // Delete a recipient
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipient(@PathVariable Long id) {
        recipientService.deleteRecipient(id);
        return ResponseEntity.ok("Recipient deleted successfully with ID: " + id);
    }
}
