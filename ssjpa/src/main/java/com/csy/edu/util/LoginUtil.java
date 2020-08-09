package com.csy.edu.util;

import java.util.HashMap;
import java.util.Map;

public class LoginUtil {
    private static LoginUtil loginUtil = null;
    /**
     * 保存登录过的用户
     */
    private static final Map<String,Map<String,String>> loginUser = new HashMap<>(0);

    private LoginUtil() {
    }

    public static LoginUtil getLoginUtil(){
        if (null == loginUtil){
            loginUtil = new LoginUtil();
        }
        return loginUtil;
    }

    /**
     * 用户登录时信心保存在map中
     * @param userName
     * @param password
     */
    public void addUser(String userName,String password){
        Map<String,String> login = new HashMap<>();
        login.put(userName,password);
        loginUser.put(userName,login);
    }

    /**
     * 根据用户名查询用户
     * @param
     * @return
     */
    public int findUser(){
       return loginUser.size();
    }
}
