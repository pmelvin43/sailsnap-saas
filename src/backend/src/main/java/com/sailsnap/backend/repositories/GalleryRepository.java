package com.sailsnap.backend.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sailsnap.backend.entities.Gallery;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Integer> {

    // find all galleries for a specific business by its ID
    List<Gallery> findByBusinessId(int businessId);

    // find a gallery by its name
    List<Gallery> findByNameContaining(String keyword);

    // find gallery created within a date range
    List<Gallery> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // sort by creation date in descending order
    List<Gallery> findAllByOrderByCreatedAtDesc();

    // filter by s3 key prefix
    List<Gallery> findByGalleryS3KeyStartingWith(String prefix);

    // find galleries by business ID and creation date range, for example:
    // "give me all galleries for business ID 1 created between 2023-01-01 and
    // 2023-01-31"
    List<Gallery> findByBusinessIdAndCreatedAtBetween(int businessId, LocalDateTime start, LocalDateTime end);

}
