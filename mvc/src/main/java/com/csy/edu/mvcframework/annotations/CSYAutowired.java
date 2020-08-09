package com.csy.edu.mvcframework.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CSYAutowired {

    String value() default "";
}
