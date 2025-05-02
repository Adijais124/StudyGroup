package com.finalstudy.controllers;

import com.finalstudy.models.StudyGroup;
import com.finalstudy.models.User;
import com.finalstudy.services.StudyGroupService;
import com.finalstudy.services.UserService;
import com.finalstudy.repositories.StudyGroupRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
public class StudyGroupController {

    @Autowired
    private StudyGroupService studyGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    // Endpoint to create a new study group
    @PostMapping("/create")
    public ResponseEntity<String> createStudyGroup(@RequestBody Map<String, String> payload, HttpSession session) {
        // Retrieve the user email from the session
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            Optional<User> userOptional = userService.findByEmail(userEmail);

            if (userOptional.isPresent()) {
                

                // Extract all required fields from the payload
                String name = payload.get("name");
                String course = payload.get("course");
                String className = payload.get("className");
                String exam = payload.get("exam");

                // Pass all arguments, including the owner ID, to the service method
                User creatorUser = userOptional.get();
                StudyGroup group = new StudyGroup();
                group.setName(name);
                group.setCourse(course);
                group.setClassName(className);
                group.setExam(exam);
                group.setOwner(creatorUser);

                return ResponseEntity.ok("Study group created successfully!");
            }
        }

        // If the user is not found or not authorized
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized!");
    }

    // Endpoint to join a study group
    @PostMapping("/join")
    public ResponseEntity<String> joinGroup(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String groupName = payload.get("groupName");

        // Fetch userId from email
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found!");
        }
        User user = userOptional.get();
        Long userId = user.getId();

        // Fetch groupId from groupName
        Optional<StudyGroup> groupOptional = studyGroupRepository.findByName(groupName);
        if (groupOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group not found!");
        }
        StudyGroup group = groupOptional.get();
        Long groupId = group.getId();

        // Call the service method
        boolean joined = studyGroupService.addUserToGroup(userId, groupId);
        if (joined) {
            return ResponseEntity.ok("User successfully joined the group!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to join the group!");
        }
    }

    // Endpoint to find study groups by course and class name
    

    // Endpoint to get all study groups
    @GetMapping("/all")
    public ResponseEntity<List<StudyGroup>> getAllGroups() {
        List<StudyGroup> groups = studyGroupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }
    @GetMapping("/groups/{groupId}")
    public String viewGroup(@PathVariable Long groupId, Model model) {
        StudyGroup group = studyGroupService.findById(groupId);
    
        if (group == null) {
            return "error-page"; // Redirect to an error page if the group isn't found
        }
    
        model.addAttribute("group", group);
        model.addAttribute("owner", group.getOwner()); //  owner (admin) 
    
        return "group-page"; 
    }


}