package com.example.donatehub.controller;

import com.example.donatehub.dto.DriveDTO;
import com.example.donatehub.dto.DriveSummaryDTO;
import com.example.donatehub.entity.Drive;
import com.example.donatehub.service.DriveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Drive operations.
 * Feature #1: Create a donation drive with start and end dates.
 * Feature #4: View total items collected and distributed per drive.
 */
@RestController
@RequestMapping("/api/drives")
public class DriveController {

    @Autowired
    private DriveService driveService;

    // Feature #1: Create a donation drive
    @PostMapping
    public ResponseEntity<Drive> createDrive(@Valid @RequestBody DriveDTO driveDTO) {
        Drive createdDrive = driveService.createDrive(driveDTO);
        return new ResponseEntity<>(createdDrive, HttpStatus.CREATED);
    }

    // Get all drives
    @GetMapping
    public ResponseEntity<List<Drive>> getAllDrives() {
        List<Drive> drives = driveService.getAllDrives();
        return ResponseEntity.ok(drives);
    }

    // Get a single drive by ID
    @GetMapping("/{id}")
    public ResponseEntity<Drive> getDriveById(@PathVariable Long id) {
        Drive drive = driveService.getDriveById(id);
        return ResponseEntity.ok(drive);
    }

    // Update a drive
    @PutMapping("/{id}")
    public ResponseEntity<Drive> updateDrive(@PathVariable Long id,
                                             @Valid @RequestBody DriveDTO driveDTO) {
        Drive updatedDrive = driveService.updateDrive(id, driveDTO);
        return ResponseEntity.ok(updatedDrive);
    }

    // Delete a drive
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDrive(@PathVariable Long id) {
        driveService.deleteDrive(id);
        return ResponseEntity.ok("Drive deleted successfully with ID: " + id);
    }

    // Feature #4: View total items collected and distributed per drive
    @GetMapping("/{id}/summary")
    public ResponseEntity<DriveSummaryDTO> getDriveSummary(@PathVariable Long id) {
        DriveSummaryDTO summary = driveService.getDriveSummary(id);
        return ResponseEntity.ok(summary);
    }
}
