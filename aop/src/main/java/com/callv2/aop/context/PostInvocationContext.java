package com.callv2.aop.context;

import java.time.Instant;

public interface PostInvocationContext extends InvocationContext {

    Instant getProceededAt();

    Object getResult();

    Throwable getThrowable();

    boolean wasSuccessful();

}