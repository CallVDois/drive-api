package com.callv2.drive.infrastructure.aop.context.aspects.chain.error;

import java.util.ArrayDeque;
import java.util.Queue;

import com.callv2.drive.infrastructure.aop.context.ExecutionErrorContext;

public abstract class AspectErrorChain {

    private AspectErrorChain next;

    protected AspectErrorChain() {
    }

    public final Throwable proceed(final ExecutionErrorContext context) throws Throwable {

        execute(context);

        if (next != null)
            return next.proceed(context);

        return context.error();

    }

    protected abstract void execute(final ExecutionErrorContext context) throws Throwable;

    private AspectErrorChain setNext(final AspectErrorChain next) {
        return this.next = next;
    }

    public static final class Builder {

        private final Queue<AspectErrorChain> chains;

        private Builder() {
            this.chains = new ArrayDeque<>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder add(final AspectErrorChain chain) {
            this.chains.add(chain);
            return this;
        }

        public AspectErrorChain build() {
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
