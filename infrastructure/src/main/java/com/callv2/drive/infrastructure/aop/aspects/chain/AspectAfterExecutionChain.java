package com.callv2.drive.infrastructure.aop.aspects.chain;

import java.util.ArrayDeque;
import java.util.Queue;

import com.callv2.drive.infrastructure.aop.aspects.context.AfterExecutionContext;

public abstract class AspectAfterExecutionChain {

    private AspectAfterExecutionChain next;

    protected AspectAfterExecutionChain() {
    }

    public final Object proceed(final AfterExecutionContext context) throws Throwable {
        execute(context);

        if (next != null)
            return next.proceed(context);

        return context.result();
    }

    protected abstract void execute(final AfterExecutionContext context) throws Throwable;

    private AspectAfterExecutionChain setNext(final AspectAfterExecutionChain next) {
        return this.next = next;
    }

    public static final class Builder {

        private final Queue<AspectAfterExecutionChain> chains;

        private Builder() {
            this.chains = new ArrayDeque<>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder add(final AspectAfterExecutionChain chain) {
            this.chains.add(chain);
            return this;
        }

        public AspectAfterExecutionChain build() {
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
