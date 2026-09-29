package com.example.donatehub.controller;

import com.example.donatehub.dto.DonorDTO;
import com.example.donatehub.entity.Donor;
import com.example.donatehub.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Donor operations.
 */
@RestController
@RequestMapping("/api/donors")
public class DonorController {

    @Autowired
    private DonorService donorService;

    // Create a new donor
    @PostMapping
    public ResponseEntity<Donor> createDonor(@Valid @RequestBody DonorDTO donorDTO) {
        Donor createdDonor = donorService.createDonor(donorDTO);
        return new ResponseEntity<>(createdDonor, HttpStatus.CREATED);
    }

    // Get all donors
    @GetMapping
    public ResponseEntity<List<Donor>> getAllDonors() {
        List<Donor> donors = donorService.getAllDonors();
        return ResponseEntity.ok(donors);
    }

    // Get a single donor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Donor> getDonorById(@PathVariable Long id) {
        Donor donor = donorService.getDonorById(id);
        return ResponseEntity.ok(donor);
    }

    // Update a donor
    @PutMapping("/{id}")
    public ResponseEntity<Donor> updateDonor(@PathVariable Long id,
                                             @Valid @RequestBody DonorDTO donorDTO) {
        Donor updatedDonor = donorService.updateDonor(id, donorDTO);
        return ResponseEntity.ok(updatedDonor);
    }

    // Delete a donor
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDonor(@PathVariable Long id) {
        donorService.deleteDonor(id);
        return ResponseEntity.ok("Donor deleted successfully with ID: " + id);
    }
}
