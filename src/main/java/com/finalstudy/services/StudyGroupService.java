package com.finalstudy.services;

import com.finalstudy.models.StudyGroup;
import com.finalstudy.repositories.StudyGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyGroupService {

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    public void createStudyGroup(String name, String course, String className, String exam) {
        StudyGroup group = new StudyGroup();
        group.setName(name);
        group.setCourse(course);
        group.setClassName(className);
        group.setExam(exam);
        group.setMaxMembers(10); // Default value for max members
        studyGroupRepository.save(group);
    }

    public boolean addUserToGroup(String email, String groupName) {
        StudyGroup group = studyGroupRepository.findByName(groupName);

        if (group != null) {
            if (group.getMembers().size() < group.getMaxMembers() && !group.getMembers().contains(email)) {
                group.getMembers().add(email);
                studyGroupRepository.save(group);
                return true;
            }
        }
        return false;
    }

    public List<StudyGroup> findByCourseAndClassName(String course, String className) {
        return studyGroupRepository.findByCourseAndClassName(course, className);
    }

    public List<StudyGroup> getAllGroups() {
        return studyGroupRepository.findAll();
    }
}