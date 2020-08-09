package com.csy.edu.mvcframework.servlet;

import com.csy.edu.mvcframework.annotations.*;
import com.csy.edu.mvcframework.pojo.Handler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSYDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();

    private List<String> classNameList = new ArrayList<>(0);

    private Map<String,Object> iocMap = new HashMap<>(0);

//    private Map<String,Method> handlerMapping = new HashMap<>(0);

    private List<Handler> handlerMapping = new ArrayList<>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //业务逻辑

        String requestURI = req.getRequestURI();
        Handler handle = getHandle(requestURI);
        if (null == handle){
            resp.getWriter().write("not found 404");
            return;
        }

        String username = req.getParameter("name");
        if (StringUtils.isBlank(username)){
            resp.getWriter().write("name can't be empty");
            return;
        }
        if (!handle.getUsers().contains(username)){
            resp.getWriter().write("user error,please check user retry");
            return;
        }

        //获取形参列表
        Class<?>[] parameterTypes = handle.getMethod().getParameterTypes();
        Object[] paramsValues = new Object[parameterTypes.length];

        Map<String, String[]> parameterMap = req.getParameterMap();
        for(Map.Entry<String,String[]> params : parameterMap.entrySet()){
            String join = StringUtils.join(params.getValue(),",");
            if (!handle.getParamIndexMapping().containsKey(params.getKey())) continue;
            Integer integer = handle.getParamIndexMapping().get(params.getKey());
            paramsValues[integer] = join;
        }
        try{
            int requestIndex = handle.getParamIndexMapping().get(HttpServletRequest.class.getSimpleName());
            paramsValues[requestIndex] = req;
            int responseIndex = handle.getParamIndexMapping().get(HttpServletResponse.class.getSimpleName());
            paramsValues[responseIndex] = resp;
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        //最终
        try {
            handle.getMethod().invoke(handle.getController(),paramsValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据url获取对应的Handler对象
     * @param requestURI
     */
    private Handler getHandle(String requestURI) {

        if (handlerMapping.isEmpty()) return null;

        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.getUrl().matcher(requestURI);
            if (matcher.matches()){
                return handler;
            }
        }
        return null;

    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        //1、加载配置文件 springmvc.properties
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);

        //2、扫描相关的注解
        doScan(properties.getProperty("scanPackage"));

        //3、初始化bean对象（实现IOC容器，基于注解）
        doInstance();

        //4、实现依赖注入
        doAutowired();

        //5、构造HandlerMapping处理器映射器，将配置好的url和method建立映射关系
        initHandlerMapping();

        System.out.println("CSY MVC INIT SUCCESS");
    }

    /**
     * 将url 和 method 建立映射关系
     */
    private void initHandlerMapping() {
        if (iocMap.isEmpty()){
            return;
        }
        for(Map.Entry<String,Object> entry : iocMap.entrySet()){
            Class<?> aClass = entry.getValue().getClass();
            if (!aClass.isAnnotationPresent(CSYRequestMapping.class)){
                continue;
            }
            CSYRequestMapping annotation = aClass.getAnnotation(CSYRequestMapping.class);
            String baseUrl = annotation.value();

            //类上配置可登录账号
            List<String> securityUsers = new ArrayList<>();
            if (aClass.isAnnotationPresent(Security.class)){
                Security security = aClass.getAnnotation(Security.class);
                List<String> strings = Arrays.asList(security.value());
                for (String string : strings) {
                    securityUsers.add(string);
                }
            }else {
//                securityUsers = new ArrayList<>();
            }

            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(CSYRequestMapping.class)){
                    continue;
                }
                CSYRequestMapping methodAnnotation = method.getAnnotation(CSYRequestMapping.class);
                String methodUrl = methodAnnotation.value();
                String url = baseUrl + methodUrl;
                Handler handler = new Handler(entry.getValue(),method, Pattern.compile(url));
                Parameter[] parameters = method.getParameters();
                for(int i = 0; i<parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    //如果参数为HttpServletRequest或者HttpServletResponse
                    if (parameter.getType() == HttpServletRequest.class || parameter.getType() == HttpServletResponse.class){
                        handler.getParamIndexMapping().put(parameter.getType().getSimpleName(),i);
                    }else {
                        handler.getParamIndexMapping().put(parameter.getName(),i);
                    }
                }
                List<String> strings = new ArrayList<>();
                if (method.isAnnotationPresent(Security.class)){
                    Security methodSecurityAnnotation = method.getAnnotation(Security.class);
                    List<String> strings1 = Arrays.asList(methodSecurityAnnotation.value());
                    for (String string : securityUsers) {
                        strings.add(string);
                    }
                   strings.addAll(strings1);
                }
                handler.setUsers(strings);
                handlerMapping.add(handler);
            }
        }
    }

    /**
     *构造HandlerMapping处理器映射器，将配置好的url和method建立映射关系
     */
    private void doAutowired() {
        if (iocMap.isEmpty()){
            return;
        }

        for(Map.Entry<String,Object> entry : iocMap.entrySet()){
            Object value = entry.getValue();
            Field[] declaredFields = value.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(CSYAutowired.class)){
                    CSYAutowired annotation = declaredField.getAnnotation(CSYAutowired.class);
                    String beanName = annotation.value();
                    if ("".equals(beanName.trim())){
                        beanName = declaredField.getType().getSimpleName();
                    }
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(entry.getValue(),iocMap.get(beanName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * 初始化bean对象（实现IOC容器，基于注解）
     */
    private void doInstance() {
        if (classNameList.isEmpty()){
            return;
        }
        try {
            for (String className : classNameList) {
                Class<?> aClass = Class.forName(className);
                if (aClass.isAnnotationPresent(CSYController.class)){
                    String simpleName = aClass.getSimpleName();
                    String beanName = lowerClassName(simpleName);
                    iocMap.put(beanName,aClass.newInstance());
                }else if (aClass.isAnnotationPresent(CSYService.class)){
                    String simpleName = aClass.getSimpleName();
                    CSYService annotation = aClass.getAnnotation(CSYService.class);
                    String value = annotation.value();
                    if ("".equals(value.trim())){
                        String beanName = lowerClassName(simpleName);
                        iocMap.put(beanName,aClass.newInstance());
                    }else {
                        iocMap.put(value,aClass.newInstance());
                    }

                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        iocMap.put(anInterface.getSimpleName(),aClass.newInstance());
                    }

                }else {
                    continue;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 首字母小写
     * @param className
     * @return
     */
    private String lowerClassName(String className){
        char[] chars = className.toCharArray();
        if ('A' <= chars[0] && chars[0] <= 'Z' ){
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    /**
     * 扫描相关的注解
     * @param packagePath
     */
    private void doScan(String packagePath) {
        String scanPackagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + packagePath.replaceAll("\\.", "/");
        File file = new File(scanPackagePath);
        File[] files = file.listFiles();
        for (File it : files){
            if (it.isDirectory()){
                doScan(packagePath + "." + it.getName());
            }else if (it.getName().endsWith(".class")){
                String className = packagePath + "." + it.getName().replaceAll(".class", "");
                classNameList.add(className);
            }
        }
    }

    /**
     * 加载配置文件 springmvc.properties
     * @param contextConfigLocation
     */
    private void doLoadConfig(String contextConfigLocation) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
