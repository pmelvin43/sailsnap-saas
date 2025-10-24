package com.sailsnap.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sailsnap.backend.entities.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {

    // find all media belonging to a specific gallery
    List<Media> findByGalleryId(int galleryId);

    // find all media belonging to a specific business
    List<Media> findByBusinessId(int businessId);

    // find all photos or videos by file type
    List<Media> findByFileType(String fileType);

    // find all watermarked media
    List<Media> findByIsWatermarkedTrue();

}
