package com.callv2.drive.infrastructure.aop.aspects.context;

import java.time.Instant;

public record ExecutionErrorContext(BeforeExecutionContext executionContext, Throwable error, Instant occuredAt) {

    public ExecutionErrorContext {
        if (executionContext == null)
            throw new IllegalArgumentException("ExecutionContext cannot be null");

        if (error == null)
            throw new IllegalArgumentException("Error cannot be null");
    }

    public static ExecutionErrorContext of(BeforeExecutionContext executionContext, Throwable error) {
        return new ExecutionErrorContext(executionContext, error, Instant.now());
    }

}
