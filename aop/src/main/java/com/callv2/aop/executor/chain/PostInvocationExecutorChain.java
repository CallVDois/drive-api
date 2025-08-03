package com.callv2.aop.executor.chain;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.executor.PostExecutor;

public final class PostInvocationExecutorChain
        extends ExecutorChain<Object, PostInvocationContext, PostExecutor> {

    public PostInvocationExecutorChain(final PostExecutor executor) {
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