package com.csy.edu.mvcframework.service.impl;

import com.csy.edu.mvcframework.annotations.CSYService;
import com.csy.edu.mvcframework.service.DemoService;

@CSYService(value = "demoService")
public class DemoServiceImpl implements DemoService {


    @Override
    public String getName(String name){
        System.out.println("name = "+ name);
        return name;
    }
}
