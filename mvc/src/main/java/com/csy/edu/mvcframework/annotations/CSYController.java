package com.csy.edu.mvcframework.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CSYController {
    String value() default "";
}
