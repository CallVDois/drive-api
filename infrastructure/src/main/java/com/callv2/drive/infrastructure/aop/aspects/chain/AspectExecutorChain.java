package com.callv2.drive.infrastructure.aop.aspects.chain;

import java.util.ArrayDeque;
import java.util.Queue;

import org.aopalliance.intercept.Joinpoint;

import com.callv2.drive.infrastructure.aop.aspects.executor.AspectExecutor;

public abstract class AspectExecutorChain<O, J extends Joinpoint, E extends AspectExecutor<J>> {

    private AspectExecutorChain<O, J, E> next;
    private final E executor;

    protected AspectExecutorChain(final E executor) {
        this.executor = executor;
    }

    public final O execute(J joinpoint) throws Throwable {

        executor.execute(joinpoint);

        if (next != null)
            next.execute(joinpoint);

        return callsProceed(joinpoint);
    }

    protected abstract O callsProceed(J joinpoint) throws Throwable;

    private AspectExecutorChain<O, J, E> setNext(AspectExecutorChain<O, J, E> next) {
        return this.next = next;
    }

    public static final class Builder<O, J extends Joinpoint, E extends AspectExecutor<J>> {

        private final Queue<AspectExecutorChain<O, J, E>> chains;

        private Builder() {
            this.chains = new ArrayDeque<>();
        }

        public static <O, J extends Joinpoint, E extends AspectExecutor<J>> Builder<O, J, E> create() {
            return new Builder<O, J, E>();
        }

        public Builder<O, J, E> add(final AspectExecutorChain<O, J, E> chain) {
            this.chains.add(chain);
            return this;
        }

        public AspectExecutorChain<O, J, E> build() {
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
