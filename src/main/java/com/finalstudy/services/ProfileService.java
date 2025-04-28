package com.finalstudy.services;

import com.finalstudy.models.Profile;
import com.finalstudy.models.User;
import com.finalstudy.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserService userService;

    // Method to update or create a profile for a user (with course synchronization)
    public void updateProfile(Long userId, String courseName, String className, String semester, String upcomingExams) {
        // Fetch the user object based on userId
        User user = userService.findById(userId);

        // Synchronize courseName in the User entity
        user.setCourse(courseName);
        userService.saveUser(user); // Update the user with the new course name

        // Check if a profile already exists for the user
        Optional<Profile> existingProfile = profileRepository.findByUserId(userId);

        if (existingProfile.isPresent()) {
            // Update existing profile
            Profile profile = existingProfile.get();
            profile.setCourseName(courseName);
            profile.setClassName(className);
            profile.setSemester(semester);
            profile.setUpcomingExams(upcomingExams);
            profileRepository.save(profile);
        } else {
            // Create a new profile if none exists
            Profile profile = new Profile();
            profile.setUser(user); // Set the associated User object
            profile.setCourseName(courseName);
            profile.setClassName(className);
            profile.setSemester(semester);
            profile.setUpcomingExams(upcomingExams);
            profileRepository.save(profile);
        }
    }

    // Method to find a profile by user
    public Profile findProfileByUser(User user) {
        return profileRepository.findByUserId(user.getId()).orElse(null);
    }

    // Additional method to save profiles
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }
}