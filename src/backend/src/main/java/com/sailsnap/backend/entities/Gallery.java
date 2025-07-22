package com.sailsnap.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gallery") // table name in the database
@Data // lombok will auto generate getters, setters, equals, hashCode, toString
@NoArgsConstructor
@AllArgsConstructor
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // primary key, auto-incremented

    @Column(name = "business_id", nullable = false)
    private int businessId; // foreign key, linking this gallery to a business

    @Column(nullable = false)
    private String name;

    @Column(name = "public_url")
    private String publicUrl;

    @Column(name = "private_url")
    private String privateUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "gallery_s3_key")
    private String galleryS3Key;
}
