package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lulu
 * 测试注解执行顺序
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
//@Profile({"dev,test"})
public @interface TestAspectOrder {

    String description() default "";
}
