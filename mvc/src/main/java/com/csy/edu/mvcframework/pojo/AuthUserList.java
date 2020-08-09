package com.csy.edu.mvcframework.pojo;

import java.util.ArrayList;

public class AuthUserList extends ArrayList<SecurityDto> {

    private static AuthUserList authUserList = null;

    private AuthUserList(){};

    public static AuthUserList getAuthUserList(){
        synchronized (Object.class){
            if (null == authUserList){
                return new AuthUserList();
            }
        }
        return authUserList;
    }
}
