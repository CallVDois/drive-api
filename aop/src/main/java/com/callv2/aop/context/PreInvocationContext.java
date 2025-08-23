package com.callv2.aop.context;

public interface PreInvocationContext extends InvocationContext {

    PostInvocationContext proceedWithContext();

}
