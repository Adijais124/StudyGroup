package com.finalstudy.controllers;

import com.finalstudy.models.StudyGroup;
import com.finalstudy.services.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class StudyGroupController {

    @Autowired
    private StudyGroupService studyGroupService;

    // Endpoint to create a new study group
    @PostMapping("/create")
    public ResponseEntity<String> createStudyGroup(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String course = payload.get("course");
        String className = payload.get("className");
        String exam = payload.get("exam");

        studyGroupService.createStudyGroup(name, course, className, exam);
        return ResponseEntity.ok("Study group created successfully!");
    }

    // Endpoint to join a study group
    @PostMapping("/join")
    public ResponseEntity<String> joinGroup(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String groupName = payload.get("groupName");

        boolean joined = studyGroupService.addUserToGroup(email, groupName);
        if (joined) {
            return ResponseEntity.ok("User successfully joined the group!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to join the group!");
        }
    }

    // Endpoint to find study groups by course and class name
    @GetMapping("/search")
    public ResponseEntity<List<StudyGroup>> searchStudyGroups(@RequestParam String course, @RequestParam String className) {
        List<StudyGroup> groups = studyGroupService.findByCourseAndClassName(course, className);
        return ResponseEntity.ok(groups);
    }

    // Endpoint to get all study groups
    @GetMapping("/all")
    public ResponseEntity<List<StudyGroup>> getAllGroups() {
        List<StudyGroup> groups = studyGroupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }
    
    
}