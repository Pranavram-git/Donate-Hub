package com.example.donatehub.controller;

import com.example.donatehub.dto.DonatedItemDTO;
import com.example.donatehub.dto.DistributionDTO;
import com.example.donatehub.entity.DonatedItem;
import com.example.donatehub.service.DonatedItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for DonatedItem operations.
 * Feature #2: Log a donated item with category, condition and donor name.
 * Feature #3: Record distribution of items to a recipient organization.
 * Feature #5: List undistributed items remaining in stock.
 */
@RestController
@RequestMapping("/api/items")
public class DonatedItemController {

    @Autowired
    private DonatedItemService donatedItemService;

    /**
     * Feature #2: Log a donated item.
     * POST /api/items
     */
    @PostMapping
    public ResponseEntity<DonatedItem> logDonatedItem(
            @Valid @RequestBody DonatedItemDTO donatedItemDTO) {
        DonatedItem createdItem = donatedItemService.logDonatedItem(donatedItemDTO);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    /**
     * Feature #3: Record distribution of an item to a recipient.
     * PUT /api/items/distribute
     */
    @PutMapping("/distribute")
    public ResponseEntity<DonatedItem> distributeItem(
            @Valid @RequestBody DistributionDTO distributionDTO) {
        DonatedItem distributedItem = donatedItemService.distributeItem(distributionDTO);
        return ResponseEntity.ok(distributedItem);
    }

    /**
     * Update an item.
     * PUT /api/items/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<DonatedItem> updateItem(@PathVariable Long id,
                                                  @Valid @RequestBody DonatedItemDTO dto) {
        DonatedItem updated = donatedItemService.updateItem(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Feature #5: List undistributed items remaining in stock.
     * GET /api/items/undistributed
     */
    @GetMapping("/undistributed")
    public ResponseEntity<List<DonatedItem>> getUndistributedItems() {
        List<DonatedItem> items = donatedItemService.getUndistributedItems();
        return ResponseEntity.ok(items);
    }

    /**
     * Get all items for a specific drive.
     * GET /api/items/drive/{driveId}
     */
    @GetMapping("/drive/{driveId}")
    public ResponseEntity<List<DonatedItem>> getItemsByDrive(
            @PathVariable Long driveId) {
        List<DonatedItem> items = donatedItemService.getItemsByDrive(driveId);
        return ResponseEntity.ok(items);
    }

    /**
     * Get all donated items.
     * GET /api/items
     */
    @GetMapping
    public ResponseEntity<List<DonatedItem>> getAllItems() {
        List<DonatedItem> items = donatedItemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    /**
     * Get a single donated item by ID.
     * GET /api/items/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<DonatedItem> getItemById(@PathVariable Long id) {
        DonatedItem item = donatedItemService.getItemById(id);
        return ResponseEntity.ok(item);
    }
}
