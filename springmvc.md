# Spring MVC

## 一、Spring MVC是什么

spring是spring提供的简化web开发的框架，实质是对servlet的封装。

## 二、Spring MVC请求处理流程

![image-20200606162157946](C:\Users\13276\AppData\Roaming\Typora\typora-user-images\image-20200606162157946.png)

**流程说明**

第一步：用户发送请求到前端控制器DispatcherServlet

第二步：DispatcherServlet收到请求调用HandlerMapping处理器映射器

第三步：处理器映射器根据Url找到具体的Handler(后端控制器)，生成处理器对象及处理器拦截器（有则生成），一并返回DispatcherServlet

第四步：DispatcherServlet调用HandlerAdapter处理器适配器去调用Handler

第五步：处理器适配器执行Handler

第六步：Handler执行完成给处理器适配器返回ModelAndView

第七步：处理器适配器给前端返回ModelAndView,ModelAndView是SpringMVC框架的一个底层对象，包括Model和View

第八步：前端控制器请求视图解析器去进行视图解析，根据逻辑视图名来解析真正的视图

第九步：视图解析器像前端返回View

第十步：前端控制器进行视图渲染，就是将模型数据（ModelAndView对象中数据）填充到request域中

第十一步：前端控制器向用户相应结果

## 三、Restful 风格

Restful是一种web软件架构风格，他既不是标准也不是协议，它倡导的是一种资源定位和资源操作的风格。简单来说就是跟据请求的Url就能够判断这个请求时干什么的。

**请求方式为：GET,POST,PUT,DELETE**

## 四、Spring MVC自定义拦截器

### 1、自定义拦截器类实现HandlerInterceptor接口

```java
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
        return false;
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
```



### 2、spring配置文件中增加

```xml
    <mvc:interceptors>
        <!--拦截所有的handler-->
        <!--<bean class="com.csy.edu.MyInterceptor"></bean>-->
        <mvc:interceptor>
            <!--配置当前拦截器的url拦截规则，**代表当前目录下及其子目录下的所有Url-->
            <mvc:mapping path="/**"/>
            <!--在mapping的基础上排除一些url拦截-->
            <mvc:exclude-mapping path="/demo/**"/>
            <bean class="com.csy.edu.MyInterceptor"></bean>
        </mvc:interceptor>
    </mvc:interceptors>
```

### 3、拦截器执行流程

如果自定义了多个自定义拦截器后，拦截器的执行顺序跟拦截器在xml中的配置顺序有关，preHandle()方法先配置，先执行。postHandle()和afterCompletion()按照配置的顺序倒叙执行。

1）程序先执行preHandle()方法，如果该方法返回true，则程序会继续向下执行处理器中的方法，否则不向下执行。

2）在业务处理器（controller类）处理完请求后，会执行postHandle()方法，然后通过DispatcherServlet向客户端返回响应

3）在DispatcherServlet处理请求完成后，才会执行afterCompletion()方法。

## 五、SpringMVC 异常处理机制

### 1、单个Cotroller配置

```java
package com.csy.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public void exception(Exception exception){
        //异常处理逻辑
    }
}

```

@ExceptionHandler可以声明需要捕获的异常类，如果需要对不同异常进行解析可以配置多**@ExceptionHandler**

### 2、全局异常处理

```java
package com.csy.edu.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionResolver {

    @ExceptionHandler({Exception.class})
    public void exceptionHandle(Exception e){
        //异常逻辑处理
    }

}

```

全局异常处理逻辑与单个controller基本一致，只需要将**@Controller**换为**@ControllerAdvice**即可。