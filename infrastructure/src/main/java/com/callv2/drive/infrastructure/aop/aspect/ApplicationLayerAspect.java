package com.callv2.drive.infrastructure.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.callv2.aop.executor.chain.handler.ExecutorChainHandler;

@Aspect
@Component
public class ApplicationLayerAspect {

    private final ExecutorChainHandler chain;

    public ApplicationLayerAspect(final ExecutorChainHandler chain) {
        this.chain = chain;
    }

    @Around("execution(* com.callv2.drive.application..*.*(..))")
    public Object aspect(ProceedingJoinPoint joinPoint) throws Throwable {
        return chain.handle(joinPoint);
    }

}
