package com.finalstudy.controllers;

import com.finalstudy.models.Comment;
import com.finalstudy.services.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notes/{noteId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<String> addComment(
            @PathVariable Long noteId,
            @RequestParam String content,
            HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.badRequest().body("User not authenticated.");
        }

        commentService.addComment(noteId, userEmail, content);
        return ResponseEntity.ok("Comment added successfully.");
    }

    @GetMapping
    public String getComments(@PathVariable Long noteId, Model model) {
        List<Comment> comments = commentService.getCommentsForNote(noteId);
        model.addAttribute("comments", comments);
        model.addAttribute("noteId", noteId);
        return "comments-section"; // Thymeleaf fragment for displaying comments
    }
}