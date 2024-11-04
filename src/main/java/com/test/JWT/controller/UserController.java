package com.test.JWT.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("User Profile");
    }

    @GetMapping("/moderate")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<String> moderateContent() {
        return ResponseEntity.ok("Moderation Content");
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> manageUsers() {
        return ResponseEntity.ok("Manage Users");
    }
}