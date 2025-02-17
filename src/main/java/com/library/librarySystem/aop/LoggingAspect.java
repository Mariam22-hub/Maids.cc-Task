package com.library.librarySystem.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.library.librarySystem.service.*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result;
        try {
            log.info("🔵 Method Called: {} | Args: {}", joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.error("Exception in Method: {} | Message: {}", joinPoint.getSignature().toShortString(), e.getMessage());
            throw e;
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        log.info("Method Completed: {} | Execution Time: {}ms", joinPoint.getSignature().toShortString(), timeTaken);

        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.library.librarySystem.service.*.*(..))", throwing = "ex")
    public void logExceptions(Exception ex) {
        log.error("Exception Thrown: {}", ex.getMessage());
    }
}