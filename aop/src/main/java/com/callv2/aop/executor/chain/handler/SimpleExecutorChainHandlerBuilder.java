package com.callv2.aop.executor.chain.handler;

import java.util.Objects;

import com.callv2.aop.executor.PostExecutor;
import com.callv2.aop.executor.PreExecutor;
import com.callv2.aop.executor.chain.PostInvocationExecutorChain;
import com.callv2.aop.executor.chain.PreInvocationExecutorChain;

public class SimpleExecutorChainHandlerBuilder {

    private PreInvocationExecutorChain preInvocationExecutorChain;
    private PostInvocationExecutorChain postInvocationExecutorChain;
    private PostInvocationExecutorChain errorInvocationExecutorChain;

    public static SimpleExecutorChainHandlerBuilder create() {
        return new SimpleExecutorChainHandlerBuilder();
    }

    public SimpleExecutorChainHandlerBuilder preExecutor(final PreExecutor executor) {

        final PreInvocationExecutorChain preInvocationExecutorChain = new PreInvocationExecutorChain(executor);

        if (Objects.isNull(this.preInvocationExecutorChain)) {
            this.preInvocationExecutorChain = preInvocationExecutorChain;
        } else {
            this.preInvocationExecutorChain = (PreInvocationExecutorChain) preInvocationExecutorChain
                    .setNext(preInvocationExecutorChain);
        }

        return this;

    }

    public SimpleExecutorChainHandlerBuilder postExecutor(final PostExecutor executor) {

        final PostInvocationExecutorChain postInvocationExecutorChain = new PostInvocationExecutorChain(executor);

        if (Objects.isNull(this.postInvocationExecutorChain)) {
            this.postInvocationExecutorChain = postInvocationExecutorChain;
        } else {
            this.postInvocationExecutorChain = (PostInvocationExecutorChain) postInvocationExecutorChain
                    .setNext(postInvocationExecutorChain);
        }

        return this;

    }

    public SimpleExecutorChainHandlerBuilder errorExecutor(final PostExecutor executor) {

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
