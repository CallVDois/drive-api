package com.callv2.drive.infrastructure.aop.aspects.context;

import java.time.Instant;

import org.aspectj.lang.JoinPoint;

public record AfterExecutionContext(Object result, JoinPoint joinPoint, Instant startedAt, Instant finishedAt) {

    public AfterExecutionContext {

        if (joinPoint == null)
            throw new IllegalArgumentException("JoinPoint cannot be null");

        if (startedAt == null)
            throw new IllegalArgumentException("StartedAt time cannot be null");

        if (finishedAt == null)
            throw new IllegalArgumentException("FinishedAt time cannot be null");

    }

    public static AfterExecutionContext of(final Object result, final BeforeExecutionContext beforeContext) {
        return new AfterExecutionContext(result, beforeContext.joinPoint(), beforeContext.startTime(), Instant.now());
    }

}
