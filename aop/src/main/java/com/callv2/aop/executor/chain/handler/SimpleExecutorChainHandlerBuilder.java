package com.callv2.aop.executor.chain.handler;

import java.util.Objects;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.context.PreInvocationContext;
import com.callv2.aop.executor.Executor;
import com.callv2.aop.executor.chain.PostInvocationExecutorChain;
import com.callv2.aop.executor.chain.PreInvocationExecutorChain;

public class SimpleExecutorChainHandlerBuilder {

    private PreInvocationExecutorChain preInvocationExecutorChain;
    private PostInvocationExecutorChain postInvocationExecutorChain;
    private PostInvocationExecutorChain errorInvocationExecutorChain;

    public static SimpleExecutorChainHandlerBuilder create() {
        return new SimpleExecutorChainHandlerBuilder();
    }

    public SimpleExecutorChainHandlerBuilder preExecutor(final Executor<PreInvocationContext> executor) {

        final PreInvocationExecutorChain preInvocationExecutorChain = new PreInvocationExecutorChain(executor);

        if (Objects.isNull(this.preInvocationExecutorChain)) {
            this.preInvocationExecutorChain = preInvocationExecutorChain;
        } else {
            this.preInvocationExecutorChain = (PreInvocationExecutorChain) preInvocationExecutorChain
                    .setNext(preInvocationExecutorChain);
        }

        return this;

    }

    public SimpleExecutorChainHandlerBuilder postExecutor(final Executor<PostInvocationContext> executor) {

        final PostInvocationExecutorChain postInvocationExecutorChain = new PostInvocationExecutorChain(executor);

        if (Objects.isNull(this.postInvocationExecutorChain)) {
            this.postInvocationExecutorChain = postInvocationExecutorChain;
        } else {
            this.postInvocationExecutorChain = (PostInvocationExecutorChain) postInvocationExecutorChain
                    .setNext(postInvocationExecutorChain);
        }

        return this;

    }

    public SimpleExecutorChainHandlerBuilder errorExecutor(final Executor<PostInvocationContext> executor) {

        final PostInvocationExecutorChain errorInvocationExecutorChain = new PostInvocationExecutorChain(executor);

        if (Objects.isNull(this.errorInvocationExecutorChain)) {
            this.errorInvocationExecutorChain = errorInvocationExecutorChain;
        } else {
            this.errorInvocationExecutorChain = (PostInvocationExecutorChain) errorInvocationExecutorChain
                    .setNext(errorInvocationExecutorChain);
        }

        return this;

    }

    public SimpleExecutorChainHandler build() {
        return new SimpleExecutorChainHandler(
                preInvocationExecutorChain,
                postInvocationExecutorChain,
                errorInvocationExecutorChain);
    }

}
