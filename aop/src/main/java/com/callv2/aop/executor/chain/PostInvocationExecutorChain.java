package com.callv2.aop.executor.chain;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.executor.Executor;

public final class PostInvocationExecutorChain
        extends ExecutorChain<Object, PostInvocationContext, Executor<PostInvocationContext>> {

    public PostInvocationExecutorChain(final Executor<PostInvocationContext> executor) {
        super(executor);
    }

    @Override
    protected Object resolve(final PostInvocationContext joinPoint) throws Throwable {
        if (joinPoint.wasSuccessful())
            return joinPoint.getResult();
        else
            throw joinPoint.getThrowable();
    }

}