package com.finalstudy.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note; // Each comment belongs to a specific note

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private LocalDateTime timestamp = LocalDateTime.now();

    public Comment() {}

    public Comment(Note note, User user, String content) {
        this.note = note;
        this.user = user;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Note getNote() { return note; }
    public User getUser() { return user; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
}