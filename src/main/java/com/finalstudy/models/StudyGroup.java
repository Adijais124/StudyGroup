package com.finalstudy.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String course;
    private String className;
    private String exam;

    private int maxMembers;

    @ElementCollection
    private List<String> members = new ArrayList<>();

    // Default constructor for JPA
    public StudyGroup() {}

    // Constructor with ID and name only
    public StudyGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructor with additional attributes
    public StudyGroup(String name, String course, String className, String exam, int maxMembers) {
        this.name = name;
        this.course = course;
        this.className = className;
        this.exam = exam;
        this.maxMembers = maxMembers;
    }

    // Full constructor for testing/mock cases
    public StudyGroup(Long id, String name, String course, String className, String exam, int maxMembers, List<String> members) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.className = className;
        this.exam = exam;
        this.maxMembers = maxMembers;
        this.members = members;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}