package com.csy.edu.mvcframework.controller;


import com.csy.edu.mvcframework.annotations.CSYAutowired;
import com.csy.edu.mvcframework.annotations.CSYController;
import com.csy.edu.mvcframework.annotations.CSYRequestMapping;
import com.csy.edu.mvcframework.annotations.Security;
import com.csy.edu.mvcframework.service.DemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CSYController
@CSYRequestMapping(value = "/demo")
@Security(value = {"chen","gong"})
public class DemoController {

    @CSYAutowired
    private DemoService demoService;

    @Security(value = "123")
    @CSYRequestMapping(value = "/getName")
    public void getName(HttpServletRequest req , HttpServletResponse res,String name){
        demoService.getName(name);
    }

    @Security(value = {"zhangsan","lisi"})
    @CSYRequestMapping(value = "/testName1")
    public void testName1(String userName){
        System.out.println(userName);
    }
}
