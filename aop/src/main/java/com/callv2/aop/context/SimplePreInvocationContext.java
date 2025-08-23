package com.callv2.aop.context;

import org.aspectj.lang.ProceedingJoinPoint;

public final class SimplePreInvocationContext extends AbstractInvocationContext implements PreInvocationContext {

    private SimplePreInvocationContext(final ProceedingJoinPoint joinPoint) {
        super(joinPoint);
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
    public PostInvocationContext proceedWithContext() {
        return SimplePostInvocationContext.from(this);
    }

}
