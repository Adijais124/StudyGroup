package com.finalstudy.repositories;

import com.finalstudy.models.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    // Fetch a study group by name
    @Query("SELECT g FROM StudyGroup g WHERE g.name = :name")
    Optional<StudyGroup> findByName(@Param("name") String name);

    // Fetch study groups by course and class name
    @Query("SELECT g FROM StudyGroup g WHERE g.course = :course AND g.className = :className")
    List<StudyGroup> findByCourseAndClassName(@Param("course") String course, @Param("className") String className);

    // Fetch a study group along with its members
    @Query("SELECT g FROM StudyGroup g LEFT JOIN FETCH g.members WHERE g.id = :id")
    Optional<StudyGroup> findByIdWithMembers(@Param("id") Long id);

    // Fetch all study groups along with their members
    @Query("SELECT g FROM StudyGroup g LEFT JOIN FETCH g.members")
    List<StudyGroup> findAllWithMembers();

    // Fetch study groups by user ID
    @Query("SELECT g FROM StudyGroup g JOIN g.members m WHERE m.id = :userId")
    List<StudyGroup> findGroupsByUserId(@Param("userId") Long userId);

    // Fetch study groups by course
    @Query("SELECT g FROM StudyGroup g WHERE g.course = :course")
    List<StudyGroup> findGroupsByCourse(@Param("course") String course);

    // Fetch suggested study groups for a user, excluding groups they are already a member of
    @Query("SELECT g FROM StudyGroup g WHERE g.course = :course AND NOT EXISTS "
         + "(SELECT m FROM g.members m WHERE m.id = :userId)")
    List<StudyGroup> findSuggestedGroups(@Param("course") String course, @Param("userId") Long userId);
}