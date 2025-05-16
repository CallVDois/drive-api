package com.callv2.drive.infrastructure.aop.aspects.chain;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

import com.callv2.drive.infrastructure.aop.aspects.context.MethodInvocationContext;
import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;
import com.callv2.drive.infrastructure.aop.aspects.executor.MethodInvocationAspectExecutor;

public final class MethodInvocationAspectExecutorChain {

    private MethodInvocationAspectExecutorChain next;
    private final MethodInvocationAspectExecutor executor;

    private MethodInvocationAspectExecutorChain(final MethodInvocationAspectExecutor executor) {
        this.executor = Objects.requireNonNull(executor, "'executor' cannot be null");
    }

    public PostInvocationContext execute(final MethodInvocationContext context) {

        executor.execute(context);

        if (next != null)
            next.execute(context);

        return context.proceedWithContext();
    }

    private MethodInvocationAspectExecutorChain setNext(final MethodInvocationAspectExecutorChain next) {
        return this.next = next;
    }

    public static final class Builder {

        private final Queue<MethodInvocationAspectExecutorChain> chains;

        private Builder() {
            this.chains = new ArrayDeque<>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder add(final MethodInvocationAspectExecutorChain chain) {
            this.chains.add(chain);
            return this;
        }

        public MethodInvocationAspectExecutorChain build() {
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
