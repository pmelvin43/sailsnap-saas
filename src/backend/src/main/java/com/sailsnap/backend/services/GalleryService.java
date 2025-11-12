package com.sailsnap.backend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sailsnap.backend.entities.Gallery;
import com.sailsnap.backend.repositories.GalleryRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    // create a new gallery for a business
    public Gallery createGallery(Long businessId, String name) {
        Gallery gallery = new Gallery();
        gallery.setBusinessId(businessId);
        gallery.setName(name);
        gallery.setCreatedAt(LocalDateTime.now());
        gallery.setUpdatedAt(LocalDateTime.now());
        gallery.setPublic(false); // default private
        // optional: generate share/public URLs here if desired later
        return galleryRepository.save(gallery);
    }

    // get a specific gallery by ID
    public Gallery getGallery(Long id) {
        return galleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery not found"));
    }

    // list all galleries for a business
    public List<Gallery> listGalleries(Long businessId) {
        return galleryRepository.findByBusinessId(businessId);
    }

    // get a shared gallery via public URL (if public)
    public Gallery getSharedGallery(String publicUrl) {
        return galleryRepository.findByPublicUrl(publicUrl)
                .orElseThrow(() -> new RuntimeException("Shared gallery not found or not public"));
    }

    // mark gallery as paid from payment service
    public void markGalleryAsPaid(Long galleryId) {
        Gallery gallery = getGallery(galleryId);
        gallery.setPaid(true);
        galleryRepository.save(gallery);
    }

    // placeholder: send gallery via email (future)
    public String sendGalleryEmail(Long galleryId, String email) {
        Gallery gallery = getGallery(galleryId);
        // TODO: implement email service integration
        log.info("Sending gallery {} to {}", gallery.getName(), email);
        return "Email sending not implemented yet";
    }
}
