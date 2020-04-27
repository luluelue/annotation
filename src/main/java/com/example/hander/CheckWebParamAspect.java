package com.example.hander;

import com.example.annotation.CheckWebParam;
import com.example.hander.pojo.CheckWebParamVo;
import com.example.pojo.Result;
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
 * 这个注解用于检测接口参数与header参数
 *
 * @author lulu
 */
@Aspect
@Component
@Order(0)
@Slf4j
public class CheckWebParamAspect {

    private static final String TOKEN = "12345";

    @Pointcut(value = "@annotation(com.example.annotation.CheckWebParam)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("获取请求体失败");
            return new Result(403, "请求失败");
        }
        HttpServletRequest request = attributes.getRequest();
        CheckWebParamVo checkWebParamVo = getAnnotationParam(joinPoint);

        if (checkWebParamVo.isCheckToken()) {
            String token = request.getHeader("token");

            if (!TOKEN.equals(token)) {
                return new Gson().toJson(new Result(403, "token 错误"));
            }
            log.info("token is        -> {}", token);
            log.info("token校验正确 -> {}", token);
        }

        if (checkWebParamVo.isCheckUserName()) {
            String userName = request.getHeader("userName");
            log.info("userName is     -> {}", userName);
            log.info("这里检查用户名");
        }

        String[] notNullParams = checkWebParamVo.getNotNull();
        for (String notNullParam : notNullParams) {
            String requestParam = request.getParameter(notNullParam);
            if (requestParam == null || "".equals(requestParam)) {
                return new Gson().toJson(new Result(403, "参数：(" + notNullParam + ") 不能为空"));
            }
        }

        return joinPoint.proceed();
    }

    public CheckWebParamVo getAnnotationParam(JoinPoint joinPoint) {
        String targetClassName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        CheckWebParamVo paramVo = new CheckWebParamVo();
        try {
            Class<?> targetClass = Class.forName(targetClassName);
            Method[] methods = targetClass.getMethods();

            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (method.getName().equals(methodName) && args.length == parameterTypes.length) {
                    paramVo.setNotNull(method.getAnnotation(CheckWebParam.class).notNull());
                    paramVo.setCheckToken(method.getAnnotation(CheckWebParam.class).checkToken());
                    paramVo.setCheckUserName(method.getAnnotation(CheckWebParam.class).checkUserName());
                }
            }

        } catch (ClassNotFoundException e) {
            log.error("Class -> {} has nor found", targetClassName, e);
        }

        return paramVo;
    }
}
