package com.example.donatehub.service;

import com.example.donatehub.dto.DonorDTO;
import com.example.donatehub.exception.ResourceNotFoundException;
import com.example.donatehub.entity.Donor;
import com.example.donatehub.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonorService {

    @Autowired
    private DonorRepository donorRepository;

    /**
     * Create a new donor.
     */
    public Donor createDonor(DonorDTO donorDTO) {
        Donor donor = new Donor();
        donor.setName(donorDTO.getName());
        donor.setEmail(donorDTO.getEmail());
        donor.setPhone(donorDTO.getPhone());
        return donorRepository.save(donor);
    }

    /**
     * Get all donors.
     */
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    /**
     * Get a single donor by ID.
     */
    public Donor getDonorById(Long id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Donor not found with ID: " + id));
    }

    /**
     * Update an existing donor.
     */
    public Donor updateDonor(Long id, DonorDTO donorDTO) {
        Donor donor = getDonorById(id);
        donor.setName(donorDTO.getName());
        donor.setEmail(donorDTO.getEmail());
        donor.setPhone(donorDTO.getPhone());
        return donorRepository.save(donor);
    }

    /**
     * Delete a donor by ID.
     */
    public void deleteDonor(Long id) {
        Donor donor = getDonorById(id);
        donorRepository.delete(donor);
    }
}
