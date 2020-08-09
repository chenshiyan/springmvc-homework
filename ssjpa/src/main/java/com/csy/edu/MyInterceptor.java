package com.csy.edu;

import com.csy.edu.util.LoginUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class MyInterceptor implements HandlerInterceptor {

    /**
     * 在Handler方法业务逻辑执行之前执行，通常用来写一写权限校验
     * @param request
     * @param response
     * @param handler
     * @return  返回boolean值代表是否放行   true 代表放行，false 代表终止
     * @throws Exception
     */
    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        int size = LoginUtil.getLoginUtil().findUser();
        String requestURI = request.getRequestURI();
        if (size<=0 && !"/resume/toLogin".equals(requestURI) && !"/resume/login".equals(requestURI)){
            response.sendRedirect("toLogin");
            return false;
        }
        return true;
    }

    /**
     * 在Handler方法业务执行之后尚未跳转页面时执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 页面已经跳转渲染完毕之后执行，
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
