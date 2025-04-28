package com.finalstudy.services;

import com.finalstudy.models.Profile;
import com.finalstudy.models.StudyGroup;
import com.finalstudy.models.User;
import com.finalstudy.repositories.ProfileRepository;
import com.finalstudy.repositories.StudyGroupRepository;
import com.finalstudy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private StudyGroupRepository groupRepository;

    // Method for registering a new user
    public User registerUser(String name, String email, String password, String course) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Hash and set the password
        user.setCourse(course); // Set the user's course
        return userRepository.save(user); // Save the user to the database
    }

    // Method for finding a user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Method for finding a user by ID
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // Method for fetching the user's study groups
    public List<StudyGroup> getUserGroups(Long userId) {
        return groupRepository.findGroupsByUserId(userId); // Fetch groups associated with the user
    }

    // Method for fetching suggested study groups based on user's course
    public List<StudyGroup> getSuggestedGroups(String course, Long userId) {
        return groupRepository.findSuggestedGroups(course, userId); // Fetch suggested groups
    }

    // Method to synchronize course in both user and profile tables
    public void updateCourse(Long userId, String newCourse) {
        User user = findById(userId); // Fetch user
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for User ID: " + userId));

        // Update the course in both entities
        user.setCourse(newCourse);
        profile.setCourseName(newCourse);

        // Save both updates
        userRepository.save(user);
        profileRepository.save(profile);
    }

    // Method to save the updated user details
    public void saveUser(User user) {
        userRepository.save(user); // Save the user to the database
    }

    // Method to validate a raw password against a hashed password
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword); // Hash the plain password
    }
    
}