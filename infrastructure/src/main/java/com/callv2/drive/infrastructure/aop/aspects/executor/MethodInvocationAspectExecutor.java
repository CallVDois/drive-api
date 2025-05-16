package com.callv2.drive.infrastructure.aop.aspects.executor;

import com.callv2.drive.infrastructure.aop.aspects.context.MethodInvocationContext;

@FunctionalInterface
public interface MethodInvocationAspectExecutor {

    void execute(MethodInvocationContext context);

}
