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

    public final O execute(final J joinpoint) throws Throwable {

        executor.execute(joinpoint);

        if (next != null)
            next.execute(joinpoint);

        return callsProceed(joinpoint);
    }

    protected abstract O callsProceed(J joinpoint) throws Throwable;

    @SuppressWarnings("unchecked")
    protected AspectExecutorChain<O, J, E> setNext(final AspectExecutorChain<?, ?, ?> next) {
        return this.next = (AspectExecutorChain<O, J, E>) next;
    }

    public static final class Builder<C extends AspectExecutorChain<?, ?, ?>> {

        private final Class<C> clazz;
        private final Queue<C> chains;

        private Builder(final Class<C> clazz) {
            this.clazz = clazz;
            this.chains = new ArrayDeque<>();
        }

        public static <C extends AspectExecutorChain<?, ?, ?>> Builder<C> create(final Class<C> clazz) {
            return new Builder<C>(clazz);
        }

        public Builder<C> add(final C chain) {

            if (chain.getClass() != clazz)
                throw new IllegalArgumentException("Chain must be exactly of type " + clazz.getName());

            this.chains.add(chain);
            return this;
        }

        @SuppressWarnings("unchecked")
        public C build() {
            if (chains.isEmpty())
                return null;

            final var firstChain = chains.poll();
            var chain = firstChain;

            do {
                chain = (C) chain.setNext(chains.poll());
            } while (!chains.isEmpty());

            return firstChain;
        }

    }

}
