package com.callv2.drive.infrastructure.aop.aspects.chain;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;

public final class PostInvocationAspectExecutorChain {

    private PostInvocationAspectExecutorChain next;
    private final PostInvocationAspectExecutorChain executor;

    private PostInvocationAspectExecutorChain(final PostInvocationAspectExecutorChain executor) {
        this.executor = Objects.requireNonNull(executor, "'executor' cannot be null");
    }

    public Object execute(final PostInvocationContext context) throws Throwable {

        executor.execute(context);

        if (next != null)
            next.execute(context);

        if (context.wasSuccessful())
            return context.getResult();
        else
            throw context.getThrowable();
    }

    private PostInvocationAspectExecutorChain setNext(final PostInvocationAspectExecutorChain next) {
        return this.next = next;
    }

    public static final class Builder {

        private final Queue<PostInvocationAspectExecutorChain> chains;

        private Builder() {
            this.chains = new ArrayDeque<>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder add(final PostInvocationAspectExecutorChain chain) {
            this.chains.add(chain);
            return this;
        }

        public PostInvocationAspectExecutorChain build() {
            if (chains.isEmpty())
                return null;

            final var firstChain = chains.poll();
            var chain = firstChain;

            do {
                chain = chain.setNext(chains.poll());
            } while (!chains.isEmpty());

            return firstChain;
        }

    }

}
