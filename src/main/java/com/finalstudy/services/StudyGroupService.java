package com.finalstudy.services;

import com.finalstudy.models.StudyGroup;
import com.finalstudy.models.User;
import com.finalstudy.repositories.StudyGroupRepository;
import com.finalstudy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudyGroupService {

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private UserRepository userRepository;

    // Method to create a new study group
    public void createStudyGroup(String name, String course, String className, String exam, Long ownerId) {
        // Fetch the owner (user) by ID and throw an exception if not found
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid owner ID"));
    
        // Validate input parameters
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty.");
        }
        if (course == null || course.isEmpty()) {
            throw new IllegalArgumentException("Course cannot be null or empty.");
        }
    
        // Create a new study group
        StudyGroup group = new StudyGroup();
        group.setName(name);
        group.setCourse(course);
        group.setClassName(className);
        group.setExam(exam);
        group.setMaxMembers(10); // Default max members
        group.setOwner(owner); // Assign the owner
        group.setMembers(new ArrayList<>()); // Initialize members list to avoid null issues
    
        // Add the owner to the group's members list
        group.getMembers().add(owner);
    
        // Save the group
        studyGroupRepository.save(group);
    }

    // Method to add a user to a study group
    public boolean addUserToGroup(Long userId, Long groupId) {
        Optional<StudyGroup> groupOptional = studyGroupRepository.findById(groupId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (groupOptional.isPresent() && userOptional.isPresent()) {
            StudyGroup group = groupOptional.get();
            User user = userOptional.get();

            // Ensure members list is initialized
            if (group.getMembers() == null) {
                group.setMembers(new ArrayList<>());
            }

            // Ensure user is not already in the group and the group has space
            if (group.getMembers().size() < group.getMaxMembers() && !group.getMembers().contains(user)) {
                group.getMembers().add(user); // Add the user to the group
                studyGroupRepository.save(group); // Save the updated group
                return true;
            }
        }
        throw new IllegalArgumentException("Invalid group or user ID, or group is full.");
    }

    // Method to remove a user from a study group
    public boolean removeUserFromGroup(Long userId, Long groupId) {
        Optional<StudyGroup> groupOptional = studyGroupRepository.findById(groupId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (groupOptional.isPresent() && userOptional.isPresent()) {
            StudyGroup group = groupOptional.get();
            User user = userOptional.get();

            // Ensure members list is initialized
            if (group.getMembers() == null) {
                group.setMembers(new ArrayList<>());
            }

            if (group.getMembers().contains(user)) {
                group.getMembers().remove(user); // Remove the user
                studyGroupRepository.save(group); // Save the updated group
                return true;
            }
        }
        throw new IllegalArgumentException("Invalid group or user ID, or user not in the group.");
    }

    // Method to find groups by course and class name
    public List<StudyGroup> findByCourseAndClassName(String course, String className) {
        List<StudyGroup> groups = studyGroupRepository.findByCourseAndClassName(course, className);
        if (groups == null) {
            groups = new ArrayList<>(); // Initialize to avoid null issues
        }
        return groups;
    }

    // Method to get all study groups
    public List<StudyGroup> getAllGroups() {
        List<StudyGroup> allGroups = studyGroupRepository.findAll();
        if (allGroups == null) {
            allGroups = new ArrayList<>(); // Initialize to avoid null issues
        }
        return allGroups;
    }
}