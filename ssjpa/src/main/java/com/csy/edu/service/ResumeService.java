package com.csy.edu.service;

import com.csy.edu.pojo.Resume;
import com.csy.edu.pojo.User;

import java.util.List;

public interface ResumeService {


    List<Resume> findAllResult();

    void saveResume(Resume resume);

    void updateResume(Resume resume);

    void deleteResume(Resume resume);

    void login(User userMap);
}
