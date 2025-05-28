package com.callv2.drive.infrastructure.aop.aspects.context;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.aopalliance.intercept.MethodInvocation;

public final class SimpleMethodInvocationContext extends AbstractMethodInvocationContext {

    private final AtomicBoolean proceeded;
    private final MethodInvocation methodInvocation;

    private SimpleMethodInvocationContext(final MethodInvocation methodInvocation) {
        super();
        this.proceeded = new AtomicBoolean(false);
        this.methodInvocation = methodInvocation;
    }

    public static SimpleMethodInvocationContext of(final MethodInvocation methodInvocation) {
        return new SimpleMethodInvocationContext(methodInvocation);
    }

    @Override
    public boolean proceeded() {
        return proceeded.get();
    }

    @Override
    @Nonnull
    public Method getMethod() {
        return methodInvocation.getMethod();
    }

    @Override
    @Nonnull
    public Object[] getArguments() {
        return methodInvocation.getArguments();
    }

    @Override
    @Nullable
    public Object proceed() throws Throwable {
        if (proceeded.getAndSet(true))
            throw new IllegalStateException("Method already proceeded");
        return methodInvocation.proceed();
    }

    @Override
    @Nullable
    public Object getThis() {
        return methodInvocation.getThis();
    }

    @Override
    @Nonnull
    public AccessibleObject getStaticPart() {
        return methodInvocation.getStaticPart();
    }

}
