package com.callv2.aop.executor.chain.handler;

import org.aspectj.lang.ProceedingJoinPoint;

@FunctionalInterface
public interface ExecutorChainHandler {

    Object handle(final ProceedingJoinPoint joinPoint) throws Throwable;

}
