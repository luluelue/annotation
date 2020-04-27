package com.example.hander;


import com.example.annotation.WebLog;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author lulu
 * 测试一下注解执行的顺序
 */
@Aspect
@Component
@Order(999)
@Slf4j
public class TestAspectOrder {

    @Pointcut(value = "@annotation(com.example.annotation.TestAspectOrder)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        String line = System.getProperty("line.separator");
        log.info("前置");
        log.info("测试注解副本的执行顺序-------------------------------" + line);

    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("--------环绕注解----环绕前----------");
        Object result = joinPoint.proceed();
        log.info("--------环绕注解----环绕后----------");

        return result;
    }

    @After("pointCut()")
    public void doAfter() {
        log.info("--------------接口参数打印结束-----------");
    }

    @AfterThrowing("pointCut()")
    public void dealException() {
        System.out.println("处理异常");
    }

}

