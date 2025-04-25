package com.finalstudy.services;

import com.finalstudy.models.Profile;
import com.finalstudy.repositories.ProfileRepository;
import com.finalstudy.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    // Method to set up a new profile for a user
    public Profile createProfile(User user, String courseName, String className, String semester, String upcomingExams) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setCourseName(courseName);
        profile.setClassName(className);
        profile.setSemester(semester);
        profile.setUpcomingExams(upcomingExams);
        return profileRepository.save(profile);
    }

    // Method to find a profile by user
    public Profile findProfileByUser(User user) {
        return profileRepository.findById(user.getId()).orElse(null);
    }
}