package com.sailsnap.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sailsnap.backend.dto.MediaResponse;
import com.sailsnap.backend.entities.Gallery;
import com.sailsnap.backend.entities.Media;
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
            @RequestParam String businessName) {

        List<MediaResponse> media = mediaService.getGalleryMedia(galleryId, businessName);
        return ResponseEntity.ok(media);
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
