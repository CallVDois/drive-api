package com.callv2.aop.context;

import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;

public interface InvocationContext extends ProceedingJoinPoint {

    Instant getContextedAt();

    boolean proceeded();

}
