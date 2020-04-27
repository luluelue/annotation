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
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class WebLogAspect {

    @Pointcut("@annotation(com.example.annotation.WebLog)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("获取请求体失败");
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String methodDescription = getAnnotationParam(joinPoint);

        //打印请求参数
        log.info("------ 开始打印请求日志 ---------");
        log.info("URL                : {}", request.getRequestURI());
        log.info("Description        : {}", methodDescription);
        log.info("HTTP Method        : {}", request.getMethod());
        log.info("Invoke Method      : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("Source IP          : {}", request.getRemoteHost());
        log.info("Request Args       : {}", new Gson().toJson(joinPoint.getArgs()));
    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        log.info("Response           : {}", new Gson().toJson(result));
        log.info("Time-Consuming     : {}", System.currentTimeMillis() - startTime);

        // 这里必须返回结果，否则接口将没有结果返回，around中也可以对response进行修饰
        return result;
    }

    @After("pointCut()")
    public void doAfter(){
        log.info("--------------接口参数打印结束-----------");
    }

    @AfterThrowing("pointCut()")
    public void dealException(){
        System.out.println("处理异常");
    }


    private String getAnnotationParam(JoinPoint joinPoint) {
        String targetClassName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        StringBuilder description = new StringBuilder();
        try {
            Class<?> targetClass = Class.forName(targetClassName);
            Method[] methods = targetClass.getMethods();

            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (method.getName().equals(methodName) && args.length == parameterTypes.length) {
                    description.append(method.getAnnotation(WebLog.class).description());
                }
            }

        } catch (ClassNotFoundException e) {
            log.error("Class -> {} has nor found", targetClassName, e);
        }

        return description.toString();
    }
}
