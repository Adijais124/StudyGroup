package com.finalstudy.repositories;

import com.finalstudy.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByNoteId(Long noteId); // Retrieve comments for a specific note
}