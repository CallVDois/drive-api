package com.callv2.drive.infrastructure.aop.aspects.chain;

import java.util.ArrayDeque;
import java.util.Queue;

import com.callv2.drive.infrastructure.aop.aspects.context.ExecutionErrorContext;

public abstract class AspectExecutionErrorChain {

    private AspectExecutionErrorChain next;

    protected AspectExecutionErrorChain() {
    }

    public final Throwable proceed(final ExecutionErrorContext context) throws Throwable {

        execute(context);

        if (next != null)
            return next.proceed(context);

        return context.error();

    }

    protected abstract void execute(final ExecutionErrorContext context) throws Throwable;

    private AspectExecutionErrorChain setNext(final AspectExecutionErrorChain next) {
        return this.next = next;
    }

    public static final class Builder {

        private final Queue<AspectExecutionErrorChain> chains;

        private Builder() {
            this.chains = new ArrayDeque<>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder add(final AspectExecutionErrorChain chain) {
            this.chains.add(chain);
            return this;
        }

        public AspectExecutionErrorChain build() {
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
