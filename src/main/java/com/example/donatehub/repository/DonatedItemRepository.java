package com.example.donatehub.repository;

import com.example.donatehub.entity.DonatedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonatedItemRepository extends JpaRepository<DonatedItem, Long> {

    // Find all items belonging to a specific drive
    List<DonatedItem> findByDriveId(Long driveId);

    // Find all items for a drive that are distributed
    List<DonatedItem> findByDriveIdAndDistributedTrue(Long driveId);

    // Find all items for a drive that are NOT distributed (undistributed / in stock)
    List<DonatedItem> findByDriveIdAndDistributedFalse(Long driveId);

    // Find all undistributed items across all drives
    List<DonatedItem> findByDistributedFalse();
}
