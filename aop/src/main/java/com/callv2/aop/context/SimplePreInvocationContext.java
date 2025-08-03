package com.callv2.aop.context;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

import org.aspectj.lang.ProceedingJoinPoint;

public final class SimplePreInvocationContext extends AbstractInvocationContext implements PreInvocationContext {

    private final Instant contextedAt;
    private final AtomicBoolean proceeded;

    private SimplePreInvocationContext(final ProceedingJoinPoint joinPoint) {
        super(joinPoint);
        this.contextedAt = Instant.now();
        this.proceeded = new AtomicBoolean(false);
    }

    public static SimplePreInvocationContext of(final ProceedingJoinPoint joinPoint) {
        return new SimplePreInvocationContext(joinPoint);
    }

    @Override
    public Object proceed() throws Throwable {
        if (proceeded.getAndSet(true))
            throw new IllegalStateException("Method already proceeded");
        return joinPoint.proceed();
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        if (proceeded.getAndSet(true))
            throw new IllegalStateException("Method already proceeded");
        return joinPoint.proceed(args);
    }

    @Override
    public Instant getContextedAt() {
        return this.contextedAt;
    }

    @Override
    public boolean proceeded() {
        return this.proceeded.get();
    }

    @Override
    public PostInvocationContext proceedWithContext() {
        return SimplePostInvocationContext.from(this);
    }

}
