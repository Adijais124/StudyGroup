package com.finalstudy.controllers;

import com.finalstudy.models.Profile;
import com.finalstudy.models.User;
import com.finalstudy.services.ProfileService;
import com.finalstudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    // Endpoint to create or update a user profile
    @PostMapping("/create")
    public ResponseEntity<String> createProfile(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        Optional<User> user = userService.findByEmail(email);
        System.out.println("Looking up user for email: " + email);
        System.out.println("User found: " + user.isPresent());
        
        if (user.isPresent()) {
            Profile profile = profileService.createProfile(
                user.get(),
                payload.get("courseName"),
                payload.get("className"),
                payload.get("semester"),
                payload.get("upcomingExams")
            );
            return ResponseEntity.ok("Profile created/updated successfully for user: " + profile.getUser().getEmail());
        } else {
            return ResponseEntity.badRequest().body("User not found!");
        }
    }

    // Endpoint to fetch a user's profile by their email
    @GetMapping("/{email}")
    public ResponseEntity<Profile> getProfileByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            Profile profile = profileService.findProfileByUser(user.get());
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}