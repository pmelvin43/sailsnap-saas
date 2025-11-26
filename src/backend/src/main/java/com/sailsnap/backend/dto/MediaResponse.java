package com.sailsnap.backend.dto;

import com.sailsnap.backend.entities.Media;
import com.sailsnap.backend.enums.FileType;

import java.time.LocalDateTime;

public class MediaResponse {
    private Long id;
    private Long businessId;
    private Long galleryId;
    private String fileKey;
    private FileType fileType;
    private LocalDateTime uploadedAt;
    private Long fileSize;
    private Boolean isWatermarked;
    private String mediaUrl; // presigned S3 URL

    public MediaResponse(Media media, String mediaUrl) {
        this.id = media.getId();
        this.businessId = media.getBusinessId();
        this.galleryId = media.getGalleryId();
        this.fileKey = media.getFileKey();
        this.fileType = media.getFileType();
        this.uploadedAt = media.getUploadedAt();
        this.fileSize = media.getFileSize();
        this.isWatermarked = media.getIsWatermarked();
        this.mediaUrl = mediaUrl;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getBusinessId() { return businessId; }
    public void setBusinessId(Long businessId) { this.businessId = businessId; }
    
    public Long getGalleryId() { return galleryId; }
    public void setGalleryId(Long galleryId) { this.galleryId = galleryId; }
    
    public String getFileKey() { return fileKey; }
    public void setFileKey(String fileKey) { this.fileKey = fileKey; }
    
    public FileType getFileType() { return fileType; }
    public void setFileType(FileType fileType) { this.fileType = fileType; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    
    public Boolean getIsWatermarked() { return isWatermarked; }
    public void setIsWatermarked(Boolean isWatermarked) { this.isWatermarked = isWatermarked; }
    
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
}