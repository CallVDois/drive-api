package com.callv2.aop.executor.chain;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.context.PreInvocationContext;
import com.callv2.aop.executor.PreExecutor;

public final class PreInvocationExecutorChain
        extends ExecutorChain<PostInvocationContext, PreInvocationContext, PreExecutor> {

    public PreInvocationExecutorChain(final PreExecutor executor) {
        super(executor);
    }

    @Override
    protected PostInvocationContext resolve(final PreInvocationContext joinPoint) throws Throwable {
        return joinPoint.proceedWithContext();
    }

}
