package com.finalstudy.services;

import com.finalstudy.models.StudyGroup;
import com.finalstudy.models.User;
import com.finalstudy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Method to hash the password
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword); // Hash the plain password
    }

    


    @Autowired
    private UserRepository userRepository;

    
    // Method for registering a new user
    public User registerUser(String name, String email, String password, String course) {
        User user = new User();
        user.setName(name);           // Set the user's name
        user.setEmail(email);         // Set the user's email
        user.setPassword(passwordEncoder.encode(password)); // Hash and set the password
        user.setCourse(course);       // Set the user's course
        return userRepository.save(user); // Save the user to the database
    }

    // Method for finding a user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Method for fetching the currently logged-in user by email
    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Example method: Fetch the user's study groups (to implement later)
    public List<StudyGroup> getUserGroups(Long userId) {
        // Logic to fetch user groups from the database can be added here
        return List.of(); // Placeholder for database logic
    }

    // Example method: Fetch suggested study groups based on user's course (to implement later)
    public List<StudyGroup> getSuggestedGroups(String course) {
        // Logic to fetch suggested groups from the database can be added here
        return List.of(); // Placeholder for database logic
    }
    

    // Method to save the updated user details
    public void saveUser(User user) {
        userRepository.save(user); // Save the user to the database
    }

   


    // Method to update user details
    public void updateUser(User user) {
        userRepository.save(user); // Update the user in the database
    }
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}