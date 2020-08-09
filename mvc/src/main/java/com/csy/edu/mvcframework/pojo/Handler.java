package com.csy.edu.mvcframework.pojo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Handler {

    /**
     * 方法对应对象
     */
    private Object controller;

    private Method method;

    private Pattern url;

    private List<String> users;
    /**
     * 参数顺序，为了进行参数绑定，key为参数名，value为第几个参数
     */
    private Map<String,Integer> paramIndexMapping;

    public Handler(Object controller, Method method, Pattern url) {
        this.controller = controller;
        this.method = method;
        this.url = url;
        users = new ArrayList<>();
        paramIndexMapping = new HashMap<>();
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

    public Pattern getUrl() {
        return url;
    }

    public void setUrl(Pattern url) {
        this.url = url;
    }

    public Map<String, Integer> getParamIndexMapping() {
        return paramIndexMapping;
    }

    public void setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
        this.paramIndexMapping = paramIndexMapping;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
