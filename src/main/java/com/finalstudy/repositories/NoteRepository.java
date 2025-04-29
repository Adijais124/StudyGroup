package com.finalstudy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finalstudy.models.Note;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // Corrected query method to retrieve notes linked to the StudyGroup's ID
    List<Note> findByGroup_Id(Long groupId);
}