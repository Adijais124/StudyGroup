package com.finalstudy.services;

import com.finalstudy.models.Note;
import com.finalstudy.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    // Retrieves notes properly based on StudyGroup ID
    public List<Note> findNotesByGroupId(Long groupId) {
        List<Note> notes = noteRepository.findByGroup_Id(groupId);
        System.out.println("✅ Notes found for Group ID " + groupId + ": " + notes.size());
        return notes;
    }

    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    public Note saveNote(Note note) {
        System.out.println("✅ Saving note with title: " + note.getTitle());
        return noteRepository.save(note);
    }
}