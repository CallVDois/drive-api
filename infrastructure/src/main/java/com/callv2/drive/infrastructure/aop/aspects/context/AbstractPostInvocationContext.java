package com.callv2.drive.infrastructure.aop.aspects.context;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractPostInvocationContext implements PostInvocationContext {

    private final Object result;
    private final Throwable throwable;
    private final Instant proceededAt;
    private final AtomicBoolean successful;

    protected AbstractPostInvocationContext(
            final Object result,
            final Throwable throwable,
            final Instant proceededAt,
            final boolean successful) {
        this.result = result;
        this.throwable = throwable;
        this.proceededAt = proceededAt;
        this.successful = new AtomicBoolean(successful);
    }

    @Override
    public Instant getProceededAt() {
        return proceededAt;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public boolean wasSuccessful() {
        return successful.get();
    }

}
