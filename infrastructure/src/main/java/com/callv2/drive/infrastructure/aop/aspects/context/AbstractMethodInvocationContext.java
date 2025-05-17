package com.callv2.drive.infrastructure.aop.aspects.context;

import java.time.Instant;

public abstract class AbstractMethodInvocationContext implements MethodInvocationContext {

    private final Instant contextedAt;

    protected AbstractMethodInvocationContext() {
        this.contextedAt = Instant.now();
    }

    @Override
    public Instant getContextedAt() {
        return contextedAt;
    }

    @Override
    public PostInvocationContext proceedWithContext() {
        return SimplePostInvocationContext.captureFromExecution(this);
    }

}
