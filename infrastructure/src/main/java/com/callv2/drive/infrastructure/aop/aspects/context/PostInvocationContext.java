package com.callv2.drive.infrastructure.aop.aspects.context;

import java.time.Instant;

import javax.annotation.Nullable;

public interface PostInvocationContext extends MethodInvocationContext {

    @Nullable
    Instant getProceededAt();

    @Nullable
    Object getResult();

    @Nullable
    Throwable getThrowable();

    boolean wasSuccessful();

}
