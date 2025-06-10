package com.callv2.drive.infrastructure.aop.aspects.context;

import java.time.Instant;

import org.aopalliance.intercept.MethodInvocation;

import jakarta.annotation.Nonnull;

public interface MethodInvocationContext extends MethodInvocation {

    @Nonnull
    Instant getContextedAt();

    boolean proceeded();

    @Nonnull
    PostInvocationContext proceedWithContext();

}
