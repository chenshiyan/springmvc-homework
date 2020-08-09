package com.csy.edu.controller;

import com.csy.edu.pojo.Resume;
import com.csy.edu.pojo.ResumeDto;
import com.csy.edu.pojo.User;
import com.csy.edu.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping(value = "login")
    private String login(String username,String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        resumeService.login(user);
        return "index";
    }
    @GetMapping("/toLogin")
    private String toLogin(){
        return "login";
    }


    @GetMapping(value = "findAllResume")
    private ModelAndView findAllResume(){
        ModelAndView modelAndView = new ModelAndView();
        List<Resume> allResult = resumeService.findAllResult();
        modelAndView.addObject("data",allResult);
        modelAndView.setViewName("resume");
        return modelAndView;
    }

    @PostMapping(value = "saveResume")
    private String saveResume(HttpServletRequest request){
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        Resume resume = new Resume();
        resume.setPhone(phone);
        resume.setName(name);
        resume.setAddress(address);
        resumeService.saveResume(resume);
        return "index";
    }

    @PostMapping(value = "updateResume")
    private String updateResume(ResumeDto resume,HttpServletRequest request){
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        Resume resume1 = new Resume();
        resume1.setId(Long.parseLong(id));
        resume1.setAddress(address);
        resume1.setName(name);
        resume1.setPhone(phone);
//        BeanUtils.copyProperties(resume,resume1);
        resumeService.updateResume(resume1);
        return "index";
    }

    @GetMapping(value = "deleteResume")
    private String deleteResume(HttpServletRequest request){
        String id = request.getParameter("id");
        Resume resume = new Resume();
        resume.setId(Long.parseLong(id));
        resumeService.deleteResume(resume);
        return "index";
    }
    @GetMapping(value = "toUpdateResume")
    private ModelAndView toUpdateResume(HttpServletRequest request){
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("updateResume");
        modelAndView.addObject("id",id);
        modelAndView.addObject("name",name);
        modelAndView.addObject("address",address);
        modelAndView.addObject("phone",phone);
        return modelAndView;
    }
    @GetMapping(value = "toAddResume")
    private String toAddResume(){
        return "addResume";
    }
}
