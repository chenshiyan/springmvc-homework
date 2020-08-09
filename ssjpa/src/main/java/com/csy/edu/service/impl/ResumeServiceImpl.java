package com.csy.edu.service.impl;

import com.csy.edu.dao.ResumeDao;
import com.csy.edu.pojo.Resume;
import com.csy.edu.pojo.User;
import com.csy.edu.service.ResumeService;
import com.csy.edu.util.LoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeDao resumeDao;
    @Override
    public List<Resume> findAllResult() {
        return resumeDao.findAll();
    }

    @Override
    public void saveResume(Resume resume) {
        resumeDao.save(resume);

    }

    @Override
    public void updateResume(Resume resume) {
        resumeDao.save(resume);
    }

    @Override
    public void deleteResume(Resume resume) {
        resumeDao.deleteById(resume.getId());
    }

    @Override
    public void login(User userMap) {
        LoginUtil loginUtil = LoginUtil.getLoginUtil();
        loginUtil.addUser(userMap.getUsername(),userMap.getPassword());
    }
}
