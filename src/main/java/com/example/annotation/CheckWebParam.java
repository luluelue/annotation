package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lulu
 * 用来检测接口参数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
//@Profile({"dev,test"})
public @interface CheckWebParam {

    /**
     * 非空参数列表
     * @return
     */
    String[] notNull() default {};

    /**
     * 是否需要校验header中的token
     * @return
     */
    boolean checkToken() default false;

    /**
     * 是否需校验用户名
     * @return
     */
    boolean checkUserName() default false;
}
