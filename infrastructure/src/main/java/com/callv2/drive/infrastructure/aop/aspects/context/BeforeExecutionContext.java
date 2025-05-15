package com.callv2.drive.infrastructure.aop.aspects.context;

import java.time.Instant;

import org.aspectj.lang.JoinPoint;

public record BeforeExecutionContext(JoinPoint joinPoint, Instant startTime) {

    public BeforeExecutionContext {

        if (joinPoint == null)
            throw new IllegalArgumentException("JoinPoint cannot be null");

        if (startTime == null)
            throw new IllegalArgumentException("Start time cannot be null");

    }

    public static BeforeExecutionContext of(JoinPoint joinPoint) {
        return new BeforeExecutionContext(joinPoint, Instant.now());
    }

}
