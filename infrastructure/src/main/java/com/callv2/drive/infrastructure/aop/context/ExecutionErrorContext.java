package com.callv2.drive.infrastructure.aop.context;

import java.time.Instant;

public record ExecutionErrorContext(ExecutionContext executionContext, Throwable error, Instant occuredAt) {

    public ExecutionErrorContext {
        if (executionContext == null)
            throw new IllegalArgumentException("ExecutionContext cannot be null");

        if (error == null)
            throw new IllegalArgumentException("Error cannot be null");
    }

    public static ExecutionErrorContext of(ExecutionContext executionContext, Throwable error) {
        return new ExecutionErrorContext(executionContext, error, Instant.now());
    }

}
