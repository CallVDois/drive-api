package com.callv2.drive.infrastructure.aop.aspects.chain;

import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;
import com.callv2.drive.infrastructure.aop.aspects.executor.AspectExecutor;

public final class PostInvocationAspectExecutorChain extends
        AspectExecutorChain<Object, PostInvocationContext, AspectExecutor<PostInvocationContext>> {

    protected PostInvocationAspectExecutorChain(final AspectExecutor<PostInvocationContext> executor) {
        super(executor);
    }

    @Override
    protected Object callsProceed(final PostInvocationContext joinpoint) throws Throwable {
        if (joinpoint.wasSuccessful())
            return joinpoint.getResult();
        else
            throw joinpoint.getThrowable();
    }

}
