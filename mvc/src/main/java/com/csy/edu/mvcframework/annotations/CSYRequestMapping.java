package com.csy.edu.mvcframework.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CSYRequestMapping {
    String value() default "";
}
