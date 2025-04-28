package com.finalstudy.controllers;

import com.finalstudy.models.Profile;
import com.finalstudy.models.User;
import com.finalstudy.services.ProfileService;
import com.finalstudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    // GET: Show user profile or render the profile page
    @GetMapping
    public String showProfile(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            Optional<User> userOptional = userService.findByEmail(userEmail);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Fetch or create the user's profile
                Profile profile = profileService.findProfileByUser(user);
                if (profile == null) {
                    profile = new Profile(user, "", "", "", ""); // Create empty profile for first-time users
                    profileService.saveProfile(profile); // Save the initial empty profile
                }

                model.addAttribute("profile", profile);
                model.addAttribute("user", user); // Pass user details to the view
                return "profile"; // Render profile.html
            }
        }

        return "redirect:/login?error=unauthorized"; // Redirect if user is not logged in
    }

    // POST: Update profile
    @PostMapping("/update")
    public String updateProfile(
        @RequestParam String name,
        @RequestParam String courseName,
        @RequestParam String className,
        @RequestParam String semester,
        @RequestParam String upcomingExams,
        HttpSession session) {

        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            Optional<User> userOptional = userService.findByEmail(userEmail);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Update the profile
                profileService.updateProfile(user.getId(), courseName, className, semester, upcomingExams);
                
                // Update user's name
                user.setName(name);
                userService.saveUser(user); // Save updated user details

                return "redirect:/profile?success=true"; // Redirect to profile with success message
            }
        }

        return "redirect:/login?error=unauthorized"; // Redirect if user is not logged in
    }

    @PostMapping("/updatePassword")
    public String updatePassword(
        @RequestParam String currentPassword,
        @RequestParam String newPassword,
        HttpSession session) {

        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            Optional<User> userOptional = userService.findByEmail(userEmail);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Validate current password
                if (userService.checkPassword(currentPassword, user.getPassword())) {
                    // Hash and update new password
                    String hashedPassword = userService.hashPassword(newPassword);
                    user.setPassword(hashedPassword);
                    userService.saveUser(user);
                    System.out.println("Password updated successfully for: " + userEmail);

                    return "redirect:/profile?success=password"; // Redirect with success message
                } else {
                    System.out.println("Current password mismatch for: " + userEmail);
                    return "redirect:/profile?error=password";
                }
            }
        }

        return "redirect:/login?error=unauthorized"; // Redirect if user is not logged in
    }
}