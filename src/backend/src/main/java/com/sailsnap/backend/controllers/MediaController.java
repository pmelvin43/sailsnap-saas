package com.sailsnap.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sailsnap.backend.entities.Media;
import com.sailsnap.backend.services.MediaService;

import java.util.List;

@RestController
@RequestMapping("/media")
@CrossOrigin(origins = "*")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    // list all media in a gallery
    @GetMapping
    public ResponseEntity<List<Media>> listGalleryMedia(@RequestParam int galleryId) {
        return ResponseEntity.ok(mediaService.listMedia(galleryId));
    }

    // upload a media file
    @PostMapping("/upload")
    public ResponseEntity<Media> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("businessId") Long businessId,
            @RequestParam("galleryId") Long galleryId,
            @RequestParam("businessName") String businessName) {

        Media savedMedia = mediaService.uploadMedia(file, businessId, galleryId, businessName);
        return ResponseEntity.ok(savedMedia);
    }

    // delete media
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedia(@PathVariable int id) {
        boolean deleted = mediaService.deleteMedia(id);
        if (deleted) {
            return ResponseEntity.ok("Media deleted successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to delete media");
        }
    }
}
