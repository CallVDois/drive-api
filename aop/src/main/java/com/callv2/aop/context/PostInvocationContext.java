package com.callv2.aop.context;

import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;

public interface PostInvocationContext extends ProceedingJoinPoint {

    Instant getProceededAt();

    Object getResult();

    Throwable getThrowable();

    boolean wasSuccessful();

}