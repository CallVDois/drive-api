package com.callv2.drive.infrastructure.aop.context;

import java.time.Instant;

import org.aspectj.lang.JoinPoint;

public record ExecutionContext(JoinPoint joinPoint, Instant startTime) {

    public ExecutionContext {

        if (joinPoint == null)
            throw new IllegalArgumentException("JoinPoint cannot be null");

        if (startTime == null)
            throw new IllegalArgumentException("Start time cannot be null");

    }

    public static ExecutionContext of(JoinPoint joinPoint) {
        return new ExecutionContext(joinPoint, Instant.now());
    }

}
