package com.sailsnap.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sailsnap.backend.entities.Business;
import com.sailsnap.backend.services.BusinessService;

import java.util.Map;

@RestController
@RequestMapping("/business")
@CrossOrigin(origins = "*") // adjust for production
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("/profile")
    public ResponseEntity<Business> getProfile(@RequestParam int id) {
        return ResponseEntity.ok(businessService.getProfile(id));
    }

    @PutMapping("/profile")
    public ResponseEntity<Business> updateLogo(@RequestParam int id, @RequestParam String logoKey) {
        return ResponseEntity.ok(businessService.updateLogo(id, logoKey));
    }

    @PutMapping("/colors")
    public ResponseEntity<Business> updateColors(
            @RequestParam int id,
            @RequestParam String primaryColor,
            @RequestParam String secondaryColor) {
        return ResponseEntity.ok(businessService.updateColors(id, primaryColor, secondaryColor));
    }

    @PostMapping("/register")
    public ResponseEntity<Business> register(@RequestBody Business business) {
        return ResponseEntity.ok(businessService.register(business));
    }

    @PostMapping("/login")
    public ResponseEntity<Business> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        return ResponseEntity.ok(businessService.login(email, password));
    }
}
