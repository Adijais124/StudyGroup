package com.finalstudy.repositories;

import com.finalstudy.models.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    @Query("SELECT g FROM StudyGroup g WHERE g.name = :name")
    StudyGroup findByName(@Param("name") String name);

    @Query("SELECT g FROM StudyGroup g WHERE g.course = :course AND g.className = :className")
    List<StudyGroup> findByCourseAndClassName(@Param("course") String course, @Param("className") String className);
}