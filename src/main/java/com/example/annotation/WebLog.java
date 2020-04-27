package com.example.annotation;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lulu
 * 用来记录接口调用
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
//@Profile({"dev,test"})
public @interface WebLog {

    String description() default "";
}
