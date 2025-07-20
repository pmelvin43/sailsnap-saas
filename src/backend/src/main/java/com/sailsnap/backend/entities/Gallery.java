package com.sailsnap.backend.entities;

import java.security.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int businessId;
    private String name;
    public String publicUrl;
    private String privateUrl;
    private Timestamp createdAt;
    private String galleryS3Key;

}
