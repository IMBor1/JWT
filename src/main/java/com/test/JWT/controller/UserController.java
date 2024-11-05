package com.test.JWT.controller;

import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
        @GetMapping("/user")
        public String user(@AuthenticationPrincipal OAuth2User principal, Model model) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("login", principal.getAttribute("login"));
            model.addAttribute("id", principal.getAttribute("id"));
            model.addAttribute("email", principal.getAttribute("email"));
            return "user";
        }
}