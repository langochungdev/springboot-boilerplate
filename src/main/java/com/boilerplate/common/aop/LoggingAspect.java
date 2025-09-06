package com.boilerplate.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.boilerplate.feature..controller..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String method = joinPoint.getSignature().toShortString();
        log.info("[REQUEST] {} args={}", method, joinPoint.getArgs());

        Object result = joinPoint.proceed();

        long time = System.currentTimeMillis() - start;
        log.info("[RESPONSE] {} result={} time={}ms", method, result, time);

        return result;
    }
}
