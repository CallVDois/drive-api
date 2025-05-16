package com.callv2.drive.infrastructure.aop.aspects.executor;

import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;

@FunctionalInterface
public interface PostInvocationContextAspectExecutor {

    void execute(PostInvocationContext context);

}
