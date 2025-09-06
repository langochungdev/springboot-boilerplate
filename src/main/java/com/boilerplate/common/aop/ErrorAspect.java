package com.boilerplate.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

@Slf4j(topic = "com.boilerplate.common.aop")
@Aspect
@Component
public class ErrorAspect {

    @AfterThrowing(
            pointcut = "execution(* com.boilerplate.feature..*(..)) || execution(* com.boilerplate.common.service..*(..))",
            throwing = "ex"
    )
    public void logError(JoinPoint joinPoint, Throwable ex) {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.error("method={} args={} exception={} timestamp={}",
                method,
                Arrays.toString(args),
                ex.getMessage(),
                Instant.now(),
                ex);
    }
}
