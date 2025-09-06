package com.boilerplate.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

//@Slf4j(topic = "com.boilerplate.audit")
@Slf4j
@Aspect
@Component
public class AuditAspect {

    @Around("execution(* com.boilerplate.feature..service..*(..))")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("method={} args={} result={} duration={}ms timestamp={}",
                    method,
                    Arrays.toString(args),
                    result,
                    duration,
                    Instant.now());
            return result;
        } catch (Throwable ex) {
            log.warn("method={} args={} error={} timestamp={}",
                    method,
                    Arrays.toString(args),
                    ex.getMessage(),
                    Instant.now());
            throw ex;
        }
    }
}
