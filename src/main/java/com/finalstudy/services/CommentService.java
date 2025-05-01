package com.finalstudy.services;

import com.finalstudy.models.Comment;
import com.finalstudy.models.Note;
import com.finalstudy.models.User;
import com.finalstudy.repositories.CommentRepository;
import com.finalstudy.repositories.NoteRepository;
import com.finalstudy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    public void addComment(Long noteId, String userEmail, String content) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        Comment comment = new Comment(note, user, content);
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsForNote(Long noteId) {
        return commentRepository.findByNoteId(noteId);
    }
}
