package com.sailsnap.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sailsnap.backend.dto.MediaResponse;
import com.sailsnap.backend.entities.Gallery;
import com.sailsnap.backend.services.GalleryService;
import com.sailsnap.backend.services.MediaService;

import java.util.List;

@RestController
@RequestMapping("/galleries")
@CrossOrigin(origins = "*")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private MediaService mediaService;

    // create a new gallery
    @PostMapping
    public ResponseEntity<Gallery> createGallery(
            @RequestParam Long businessId,
            @RequestParam String name) {

        Gallery gallery = galleryService.createGallery(businessId, name);
        return ResponseEntity.ok(gallery);
    }

    // get a gallery by ID
    @GetMapping("/{id}")
    public ResponseEntity<Gallery> getGallery(@PathVariable Long id) {
        return ResponseEntity.ok(galleryService.getGallery(id));
    }

    // list all galleries for a business
    @PostMapping("/list-galleries")
    public ResponseEntity<List<Gallery>> listGalleries(@RequestParam Long businessId) {
        return ResponseEntity.ok(galleryService.listGalleries(businessId));
    }

    // get media for a gallery
    @GetMapping("/{galleryId}/media")
    public ResponseEntity<List<MediaResponse>> getGalleryMedia(
            @PathVariable long galleryId,
            @RequestParam Long businessId) { // ← Changed from businessName to businessId

        List<MediaResponse> media = mediaService.getGalleryMedia(galleryId, businessId);
        return ResponseEntity.ok(media);
    }

    // get media URLs directly from S3 (bypasses database)
    @GetMapping("/{galleryId}/media-urls")
    public ResponseEntity<List<String>> getGalleryMediaUrlsDirect(
            @PathVariable long galleryId,
            @RequestParam Long businessId) { // ← Add this new endpoint if needed

        List<String> mediaUrls = mediaService.getGalleryMediaUrlsDirect(businessId, galleryId);
        return ResponseEntity.ok(mediaUrls);
    }

    // placeholder for sending gallery email
    @PostMapping("/send-gallery-email")
    public ResponseEntity<String> sendGalleryEmail(
            @RequestParam Long galleryId,
            @RequestParam String email) {

        String result = galleryService.sendGalleryEmail(galleryId, email);
        return ResponseEntity.ok(result);
    }
}