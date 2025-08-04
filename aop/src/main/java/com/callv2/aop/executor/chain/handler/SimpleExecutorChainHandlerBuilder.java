package com.callv2.aop.executor.chain.handler;

import java.util.Queue;

import org.aspectj.lang.ProceedingJoinPoint;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.context.PreInvocationContext;
import com.callv2.aop.executor.Executor;
import com.callv2.aop.executor.PostExecutor;
import com.callv2.aop.executor.PreExecutor;
import com.callv2.aop.executor.chain.ExecutorChain;
import com.callv2.aop.executor.chain.PostInvocationExecutorChain;
import com.callv2.aop.executor.chain.PreInvocationExecutorChain;

public class SimpleExecutorChainHandlerBuilder {

    private final Queue<ExecutorChain<PostInvocationContext, PreInvocationContext, PreExecutor>> preInvocationExecutorChainQueue;
    private final Queue<ExecutorChain<Object, PostInvocationContext, PostExecutor>> postInvocationExecutorChainQueue;
    private final Queue<ExecutorChain<Object, PostInvocationContext, PostExecutor>> errorInvocationExecutorChainQueue;

    public SimpleExecutorChainHandlerBuilder() {
        this.preInvocationExecutorChainQueue = new java.util.LinkedList<>();
        this.postInvocationExecutorChainQueue = new java.util.LinkedList<>();
        this.errorInvocationExecutorChainQueue = new java.util.LinkedList<>();
    }

    public static SimpleExecutorChainHandlerBuilder create() {
        return new SimpleExecutorChainHandlerBuilder();
    }

    public SimpleExecutorChainHandlerBuilder preExecutor(final PreExecutor executor) {
        this.preInvocationExecutorChainQueue.add(new PreInvocationExecutorChain(executor));
        return this;
    }

    public SimpleExecutorChainHandlerBuilder postExecutor(final PostExecutor executor) {
        this.postInvocationExecutorChainQueue.add(new PostInvocationExecutorChain(executor));
        return this;
    }

    public SimpleExecutorChainHandlerBuilder errorExecutor(final PostExecutor executor) {
        this.errorInvocationExecutorChainQueue.add(new PostInvocationExecutorChain(executor));
        return this;
    }

    private static PreInvocationExecutorChain buildPreInvocationChain(
            final Queue<ExecutorChain<PostInvocationContext, PreInvocationContext, PreExecutor>> chains) {

        return (PreInvocationExecutorChain) buildInvocationChain(chains);
    }

    private PostInvocationExecutorChain buildPostInvocationChain(
            Queue<ExecutorChain<Object, PostInvocationContext, PostExecutor>> chains) {

        return (PostInvocationExecutorChain) buildInvocationChain(chains);
    }

    private static <O extends Object, J extends ProceedingJoinPoint, E extends Executor<J>> ExecutorChain<O, J, E> buildInvocationChain(
            final Queue<ExecutorChain<O, J, E>> chains) {
        if (chains.isEmpty())
            return null;

        final var firstChain = chains.poll();
        var chain = firstChain;

        do {
            final var next = chains.poll();
            chain = chain.setNext(next);
        } while (!chains.isEmpty());

        return firstChain;
    }

    public SimpleExecutorChainHandler build() {

        return new SimpleExecutorChainHandler(
                buildPreInvocationChain(preInvocationExecutorChainQueue),
                buildPostInvocationChain(postInvocationExecutorChainQueue),
                buildPostInvocationChain(errorInvocationExecutorChainQueue));
    }

}
