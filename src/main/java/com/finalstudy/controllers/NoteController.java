package com.finalstudy.controllers;

import com.finalstudy.models.Note;
import com.finalstudy.models.StudyGroup;
import com.finalstudy.models.User;
import com.finalstudy.services.NoteService;
import com.finalstudy.services.StudyGroupService;
import com.finalstudy.repositories.UserRepository;
import com.finalstudy.models.Comment;
import com.finalstudy.services.CommentService;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private StudyGroupService studyGroupService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentService commentService;

    @GetMapping("/groups/{groupId}/notes")
    public String viewNotes(@PathVariable Long groupId, Model model) {
        StudyGroup group = studyGroupService.findById(groupId);
        List<Note> notes = noteService.findNotesByGroupId(groupId);

        // Ensure comments are retrieved with each note
        for (Note note : notes) {
            List<Comment> comments = commentService.getCommentsForNote(note.getId());
            note.setComments(comments);
        }

        model.addAttribute("group", group);
        model.addAttribute("notes", notes);
        return "notes-page";
    }

    @PostMapping("/groups/{groupId}/notes/upload")
    public String uploadNote(
            @PathVariable Long groupId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam("file") MultipartFile file,
            HttpSession session) {

        // Check if userEmail is in session
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            System.out.println("Upload failed: No user session found.");
            return "redirect:/login?error=unauthorized&message=Please log in to upload notes.";
        }

        StudyGroup group = studyGroupService.findById(groupId);
        User uploader = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setGroup(group);
        note.setUploader(uploader);
        note.setTitle(title);
        note.setContent(content);

        try {
            note.setFileName(file.getOriginalFilename());
            note.setFileData(file.getBytes());
            System.out.println("File uploaded: " + note.getFileName() + ", Size: " + note.getFileData().length + " bytes");
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/groups/" + groupId + "?error=fileUploadFailed";
        }

        noteService.saveNote(note);
        return "redirect:/groups/" + groupId;
    }
    @GetMapping("/groups/{groupId}/notes-page")
    public String viewNotesPage(@PathVariable Long groupId, Model model) {
        StudyGroup group = studyGroupService.findById(groupId);
        if (group == null) {
            throw new RuntimeException("Group not found.");
        }
    
        List<Note> notes = noteService.findNotesByGroupId(groupId);
    
        for (Note note : notes) {
            List<Comment> comments = commentService.getCommentsForNote(note.getId());
            note.getComments().clear();
            note.getComments().addAll(comments);
        }
    
        model.addAttribute("group", group);
        model.addAttribute("notes", notes);
        return "notes-page";
    }

    @GetMapping("/groups/{groupId}/notes/{noteId}/file")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable Long groupId,
            @PathVariable Long noteId,
            HttpSession session) {

        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            System.out.println("Download attempt blocked: No user session.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Check if the user is part of the group
        StudyGroup group = studyGroupService.findById(groupId);
        if (group.getMembers() == null || group.getMembers().isEmpty()) {
            System.out.println("Group has no members or null member list.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        boolean isMember = group.getMembers().stream()
                .anyMatch(member -> member.getEmail().equals(userEmail));
        if (!isMember) {
            System.out.println("User " + userEmail + " is not a member of group " + groupId + ".");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Fetch the note and file
        Note note = noteService.findById(noteId)
                .orElse(null);

        if (note == null) {
            System.out.println("Error: Note ID " + noteId + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (note.getFileData() == null || note.getFileData().length == 0) {
            System.out.println("Error: File data missing for Note ID " + noteId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        System.out.println("Serving file: " + note.getFileName() + " to user " + userEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + note.getFileName() + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(note.getFileData());
    }
}