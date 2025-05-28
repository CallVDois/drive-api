package com.callv2.drive.infrastructure.aop.aspects.context;

import java.time.Instant;

import javax.annotation.Nonnull;

import org.aopalliance.intercept.MethodInvocation;

public interface MethodInvocationContext extends MethodInvocation {

    @Nonnull
    Instant getContextedAt();

    boolean proceeded();

    @Nonnull
    PostInvocationContext proceedWithContext();

}
