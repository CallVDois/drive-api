package com.callv2.drive.infrastructure.aop.aspects.chain;

import com.callv2.drive.infrastructure.aop.aspects.context.MethodInvocationContext;
import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;
import com.callv2.drive.infrastructure.aop.aspects.executor.AspectExecutor;

public final class MethodInvocationAspectExecutorChain extends
        AspectExecutorChain<PostInvocationContext, MethodInvocationContext, AspectExecutor<MethodInvocationContext>> {

    protected MethodInvocationAspectExecutorChain(final AspectExecutor<MethodInvocationContext> executor) {
        super(executor);
    }

    @Override
    protected PostInvocationContext callsProceed(final MethodInvocationContext joinpoint) throws Throwable {
        return joinpoint.proceedWithContext();
    }

}
