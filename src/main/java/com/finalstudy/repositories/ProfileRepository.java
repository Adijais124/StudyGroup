package com.finalstudy.repositories;

import com.finalstudy.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    // Fetch a Profile by userId
    @Query("SELECT p FROM Profile p WHERE p.user.id = :userId")
    Optional<Profile> findByUserId(@Param("userId") Long userId);

    // Additional utility methods (if needed in the future)
    // e.g., Fetch all profiles linked to a specific course
    // @Query("SELECT p FROM Profile p WHERE p.courseName = :courseName")
    // List<Profile> findProfilesByCourse(@Param("courseName") String courseName);
}