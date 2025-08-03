package com.callv2.aop.executor.chain;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.context.PreInvocationContext;
import com.callv2.aop.executor.Executor;

public class PreInvocationExecutorChain
        extends ExecutorChain<PostInvocationContext, PreInvocationContext, Executor<PreInvocationContext>> {

    public PreInvocationExecutorChain(final Executor<PreInvocationContext> executor) {
        super(executor);
    }

    @Override
    protected PostInvocationContext resolve(final PreInvocationContext joinPoint) throws Throwable {
        return joinPoint.proceedWithContext();
    }

}
