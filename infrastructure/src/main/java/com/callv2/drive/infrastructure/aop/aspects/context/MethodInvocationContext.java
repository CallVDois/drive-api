package com.callv2.drive.infrastructure.aop.aspects.context;

import java.time.Instant;

import jakarta.annotation.Nonnull;

import org.aopalliance.intercept.MethodInvocation;

public interface MethodInvocationContext extends MethodInvocation {

    @Nonnull
    Instant getContextedAt();

    @Nonnull
    boolean proceeded();

    @Nonnull
    PostInvocationContext proceedWithContext();

}
