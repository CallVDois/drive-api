package com.callv2.drive.infrastructure.aop.aspects.context;

import java.time.Instant;

import javax.annotation.Nonnull;

public abstract class AbstractMethodInvocationContext implements MethodInvocationContext {

    private final Instant contextedAt;

    protected AbstractMethodInvocationContext() {
        this.contextedAt = Instant.now();
    }

    @Override
    @Nonnull
    public Instant getContextedAt() {
        return contextedAt;
    }

    @Override
    @Nonnull
    public PostInvocationContext proceedWithContext() {
        return SimplePostInvocationContext.captureFromExecution(this);
    }

}
