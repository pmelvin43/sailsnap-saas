package com.sailsnap.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sailsnap.backend.entities.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

    // find all media belonging to a specific gallery
    List<Media> findByGalleryId(long galleryId);

    // find all media belonging to a specific business
    List<Media> findByBusinessId(long businessId);

    // find all photos or videos by file type
    List<Media> findByFileType(String fileType);

    // find all watermarked media
    List<Media> findByIsWatermarkedTrue();

}
