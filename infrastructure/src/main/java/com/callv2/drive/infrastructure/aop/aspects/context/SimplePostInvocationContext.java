package com.callv2.drive.infrastructure.aop.aspects.context;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SimplePostInvocationContext extends AbstractPostInvocationContext {

    private final MethodInvocationContext methodInvocationContext;

    private SimplePostInvocationContext(
            final Object result,
            final Throwable throwable,
            final Instant proceededAt,
            final boolean successful,
            final MethodInvocationContext methodInvocationContext) {
        super(result, throwable, proceededAt, successful);
        this.methodInvocationContext = Objects.requireNonNull(methodInvocationContext,
                "'methodInvocationContext' must not be null");
    }

    public static final PostInvocationContext captureFromExecution(
            final MethodInvocationContext methodInvocationContext) {

        Objects.requireNonNull(methodInvocationContext, "'methodInvocationContext' must not be null");

        Object result;
        Throwable throwable;
        boolean successful;
        final Instant proceededAt;

        try {
            result = methodInvocationContext.proceed();
            throwable = null;
            successful = true;
        } catch (Throwable e) {
            result = null;
            throwable = e;
            successful = false;
        } finally {
            proceededAt = Instant.now();
        }

        return new SimplePostInvocationContext(
                result,
                throwable,
                proceededAt,
                successful,
                methodInvocationContext);

    }

    @Override
    public Instant getContextedAt() {
        return methodInvocationContext.getContextedAt();
    }

    @Override
    public boolean proceeded() {
        return methodInvocationContext.proceeded();
    }

    @Override
    public PostInvocationContext proceedWithContext() {
        return this;
    }

    @Override
    @Nonnull
    public Method getMethod() {
        return methodInvocationContext.getMethod();
    }

    @Override
    @Nonnull
    public Object[] getArguments() {
        return methodInvocationContext.getArguments();
    }

    @Override
    @Nullable
    public Object proceed() throws Throwable {
        return methodInvocationContext.proceed();
    }

    @Override
    @Nullable
    public Object getThis() {
        return methodInvocationContext.getThis();
    }

    @Override
    @Nonnull
    public AccessibleObject getStaticPart() {
        return methodInvocationContext.getStaticPart();
    }

}
