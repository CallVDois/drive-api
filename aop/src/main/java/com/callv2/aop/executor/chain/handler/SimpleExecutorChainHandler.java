package com.callv2.aop.executor.chain.handler;

import org.aspectj.lang.ProceedingJoinPoint;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.context.SimplePreInvocationContext;
import com.callv2.aop.executor.chain.PostInvocationExecutorChain;
import com.callv2.aop.executor.chain.PreInvocationExecutorChain;

public final class SimpleExecutorChainHandler implements ExecutorChainHandler {

    private final PreInvocationExecutorChain preInvocationExecutorChain;
    private final PostInvocationExecutorChain postInvocationExecutorChain;
    private final PostInvocationExecutorChain errorInvocationExecutorChain;

    private final PreInvocationExecutorChain noOpPreInvocationExecutorChain = new PreInvocationExecutorChain(j -> {
    });

    private final PostInvocationExecutorChain noOpPostInvocationExecutorChain = new PostInvocationExecutorChain(j -> {
    });

    public SimpleExecutorChainHandler(
            final PreInvocationExecutorChain preInvocationExecutorChain,
            final PostInvocationExecutorChain postInvocationExecutorChain,
            final PostInvocationExecutorChain errorInvocationExecutorChain) {
        this.preInvocationExecutorChain = preInvocationExecutorChain == null ? noOpPreInvocationExecutorChain
                : preInvocationExecutorChain;
        this.postInvocationExecutorChain = postInvocationExecutorChain == null ? noOpPostInvocationExecutorChain
                : postInvocationExecutorChain;
        this.errorInvocationExecutorChain = errorInvocationExecutorChain == null ? noOpPostInvocationExecutorChain
                : errorInvocationExecutorChain;
    }

    @Override
    public Object handle(final ProceedingJoinPoint joinPoint) throws Throwable {

        final SimplePreInvocationContext preContext = SimplePreInvocationContext.of(joinPoint);
        final PostInvocationContext postContext = preInvocationExecutorChain.execute(preContext);

        if (postContext.wasSuccessful())
            return postInvocationExecutorChain.execute(postContext);
        else
            errorInvocationExecutorChain.execute(postContext);

        final Throwable throwable = postContext.getThrowable();
        if (throwable != null)
            throw throwable;

        throw new IllegalStateException("Invocation failed but no throwable was provided.");

    }

}
