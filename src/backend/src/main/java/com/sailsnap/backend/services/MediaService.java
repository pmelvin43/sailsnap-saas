package com.sailsnap.backend.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sailsnap.backend.entities.Media;
import com.sailsnap.backend.enums.FileType;
import com.sailsnap.backend.repositories.MediaRepository;
import com.sailsnap.backend.repositories.S3Repository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private S3Repository s3Repository;

    // need to be able to upload media to S3 and save it to the DB
    public Media uploadMedia(MultipartFile file, Long businessId, Long galleryId, String businessName) {
        try (InputStream inputStream = file.getInputStream()) {
            String contentType = file.getContentType();
            long fileSize = file.getSize();

            // determine if it's a photo or video
            FileType fileType = contentType != null && contentType.startsWith("video")
                    ? FileType.VIDEO
                    : FileType.PHOTO;

            // save to S3
            String s3Key = s3Repository.saveFile(
                    inputStream,
                    "gallery-" + galleryId,
                    contentType,
                    fileSize,
                    businessName);

            // create and save the media entity
            Media media = new Media();
            media.setBusinessId(businessId);
            media.setGalleryId(galleryId);
            media.setFileKey(s3Key);
            media.setFileType(fileType);
            media.setUploadedAt(LocalDateTime.now());
            media.setFileSize(fileSize);
            media.setIsWatermarked(false);

            return mediaRepository.save(media);

        } catch (IOException e) {
            log.error("Error uploading media for business {}: {}", businessId, e.getMessage(), e);
            throw new RuntimeException("Failed to upload media", e);
        }
    }

    // need to be able to list media
    public List<Media> listMedia(int galleryId) {
        return mediaRepository.findByGalleryId(galleryId);
    }

    // need to be able to delete media
    public boolean deleteMedia(int id) {
        mediaRepository.deleteById(id);
        return true;
    }
}
