package com.csy.edu.mvcframework.pojo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SecurityDto {

    /**
     * 方法对应对象
     */
    private Object controller;

    private Method method;

    private Pattern url;

    private List<String> userNames;

    public SecurityDto(Object controller,Method method,Pattern url){
        this.controller = controller;
        this.method = method;
        this.url = url;
        this.userNames = new ArrayList<>();
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public Pattern getUrl() {
        return url;
    }

    public void setUrl(Pattern url) {
        this.url = url;
    }
}
