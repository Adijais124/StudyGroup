package com.finalstudy.controllers;

import com.finalstudy.models.StudyGroup;
import com.finalstudy.models.User;
import com.finalstudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Retrieve the logged-in user's email from the session
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            // Fetch the user details using their email
            Optional<User> userOptional = userService.findByEmail(userEmail);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Add user details to the model
                model.addAttribute("user", user);

                // Fetch and add the user's study groups to the model
                List<StudyGroup> userGroups = userService.getUserGroups(user.getId());
                model.addAttribute("userGroups", userGroups);

                // Fetch and add the suggested study groups to the model
                List<StudyGroup> suggestedGroups = userService.getSuggestedGroups(user.getCourse());
                model.addAttribute("suggestedGroups", suggestedGroups);

                // Render the dashboard view
                return "dashboard";
            }
        }

        // Redirect to login if user is not authenticated
        return "redirect:/login?error=unauthorized";
    }
}