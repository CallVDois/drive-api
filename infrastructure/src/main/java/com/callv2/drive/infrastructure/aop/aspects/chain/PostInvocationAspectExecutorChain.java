package com.callv2.drive.infrastructure.aop.aspects.chain;

import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;
import com.callv2.drive.infrastructure.aop.aspects.executor.AspectExecutor;

public final class PostInvocationAspectExecutorChain extends
        AspectExecutorChain<Object, PostInvocationContext, AspectExecutor<PostInvocationContext>> {

    private PostInvocationAspectExecutorChain(final AspectExecutor<PostInvocationContext> executor) {
        super(executor);
    }

    public static PostInvocationAspectExecutorChain with(final AspectExecutor<PostInvocationContext> executor) {
        return new PostInvocationAspectExecutorChain(executor);
    }

    @Override
    protected Object callsProceed(final PostInvocationContext joinpoint) throws Throwable {
        if (joinpoint.wasSuccessful())
            return joinpoint.getResult();
        else
            throw joinpoint.getThrowable();
    }

}
