package com.finalstudy.controllers;

import com.finalstudy.models.User;
import com.finalstudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");     // Extract 'name' from the payload
        String email = payload.get("email");  // Extract 'email' from the payload
        String password = payload.get("password"); // Extract 'password' from the payload
        String course = payload.get("course");    // Extract 'course' from the payload

        // Check if the user already exists by email
        Optional<User> existingUser = userService.findByEmail(email);
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        // Register the user with all fields
        userService.registerUser(name, email, password, course);
        return ResponseEntity.ok("User registered successfully!");
    }

    // Endpoint for finding a user by email
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}