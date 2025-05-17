package com.callv2.drive.infrastructure.aop.aspects.chain;

import com.callv2.drive.infrastructure.aop.aspects.context.MethodInvocationContext;
import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;
import com.callv2.drive.infrastructure.aop.aspects.executor.AspectExecutor;

public final class MethodInvocationAspectExecutorChain extends
        AspectExecutorChain<PostInvocationContext, MethodInvocationContext, AspectExecutor<MethodInvocationContext>> {

    private MethodInvocationAspectExecutorChain(final AspectExecutor<MethodInvocationContext> executor) {
        super(executor);
    }

    public static MethodInvocationAspectExecutorChain with(final AspectExecutor<MethodInvocationContext> executor) {
        return new MethodInvocationAspectExecutorChain(executor);
    }

    @Override
    protected PostInvocationContext callsProceed(final MethodInvocationContext joinpoint) throws Throwable {
        return joinpoint.proceedWithContext();
    }

}
