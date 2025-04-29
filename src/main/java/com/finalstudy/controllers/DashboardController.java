package com.finalstudy.controllers;

import com.finalstudy.models.Profile;
import com.finalstudy.models.StudyGroup;
import com.finalstudy.models.User;
import com.finalstudy.services.ProfileService;
import com.finalstudy.services.StudyGroupService;
import com.finalstudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private StudyGroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    // Helper method to initialize and sanitize groups list
    private List<StudyGroup> initializeGroups(List<StudyGroup> groups) {
        if (groups == null) {
            return new ArrayList<>();
        }
        groups.removeIf(group -> group == null); // Remove null groups
        for (StudyGroup group : groups) {
            if (group.getMembers() == null) {
                group.setMembers(new ArrayList<>()); // Initialize members list
            }
        }
        return groups;
    }

    // Dashboard endpoint
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            Optional<User> userOptional = userService.findByEmail(userEmail);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("user", user);

                // Fetch and add profile data
                Profile profile = profileService.findProfileByUser(user);
                model.addAttribute("profile", profile);

                // Fetch "My Groups"
                List<StudyGroup> userGroups = initializeGroups(userService.getUserGroups(user.getId()));
                model.addAttribute("userGroups", userGroups);

                // Fetch "Suggested Groups"
                List<StudyGroup> suggestedGroups = userService.getSuggestedGroups(user.getCourse(), user.getId());
                model.addAttribute("suggestedGroups", suggestedGroups);

                return "dashboard";
            }
        }

        return "redirect:/login?error=unauthorized&message=Session expired. Please log in again.";
    }

    // Join a study group
    @GetMapping("/groups/{groupId}/join")
    public String joinGroup(@PathVariable Long groupId, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            Optional<User> userOptional = userService.findByEmail(userEmail);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                groupService.addUserToGroup(user.getId(), groupId); // Add user to group
                return "redirect:/dashboard?success=join";
            }
        }

        return "redirect:/login?error=unauthorized";
    }

    // Leave a study group
    @GetMapping("/groups/{groupId}/leave")
    public String leaveGroup(@PathVariable Long groupId, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            Optional<User> userOptional = userService.findByEmail(userEmail);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                groupService.removeUserFromGroup(user.getId(), groupId); // Remove user from group
                return "redirect:/dashboard?success=leave";
            }
        }

        return "redirect:/login?error=unauthorized";
    }

    // Create a new study group
    @PostMapping("/groups/create")
    public String createGroup(@RequestParam String groupName, @RequestParam String course,
                              @RequestParam String className, @RequestParam String exam, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            Optional<User> userOptional = userService.findByEmail(userEmail);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                groupService.createStudyGroup(groupName, course, className, exam, user.getId());
                return "redirect:/dashboard?success=create";
            }
        }

        return "redirect:/login?error=unauthorized";
    }

    // Navigate to group page
    @GetMapping("/groups/{groupId}")
public String viewGroup(@PathVariable Long groupId, Model model) {
    // Directly fetch the group, throwing an exception if not found
    StudyGroup group = groupService.findById(groupId);

    // Pass the group to the model for rendering on the group page
    model.addAttribute("group", group);
    return "group-page"; // Render the group-page template
}
}