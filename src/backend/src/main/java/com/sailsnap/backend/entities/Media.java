package com.sailsnap.backend.entities;

import java.time.LocalDateTime;

import com.sailsnap.backend.enums.FileType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "gallery_id", nullable = false)
    private Long galleryId;

    @Column(name = "file_key", unique = true)
    private String fileKey; // used to reference in S3 (UUID, etc.)

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileType fileType; // enum for 'PHOTO' or 'VIDEO'

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "media_runtime")
    private Double mediaRuntime;

    @Column(name = "is_watermarked")
    private Boolean isWatermarked = false;
}
