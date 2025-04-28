package com.finalstudy.models;

import jakarta.persistence.*;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String courseName;
    private String className;
    private String semester;
    private String upcomingExams;

    // Default constructor for JPA
    public Profile() {}

    // Constructor for creating/updating profiles
    public Profile(User user, String courseName, String className, String semester, String upcomingExams) {
        this.user = user;
        this.courseName = courseName;
        this.className = className;
        this.semester = semester;
        this.upcomingExams = upcomingExams;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getUpcomingExams() {
        return upcomingExams;
    }

    public void setUpcomingExams(String upcomingExams) {
        this.upcomingExams = upcomingExams;
    }

    // Additional utility methods if needed (e.g., toString)
    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", user=" + user +
                ", courseName='" + courseName + '\'' +
                ", className='" + className + '\'' +
                ", semester='" + semester + '\'' +
                ", upcomingExams='" + upcomingExams + '\'' +
                '}';
    }
}