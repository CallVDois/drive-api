package com.callv2.drive.infrastructure.aop.context.aspects.chain.execution;

import java.util.ArrayDeque;
import java.util.Queue;

import org.aspectj.lang.ProceedingJoinPoint;

import com.callv2.drive.infrastructure.aop.context.ExecutionContext;

public abstract class AspectExecutionChain {

    private AspectExecutionChain next;

    protected AspectExecutionChain() {
    }

    public final Object proceed(final ExecutionContext context) throws Throwable {
        execute(context);

        if (next != null)
            return next.proceed(context);

        if (context.joinPoint() instanceof ProceedingJoinPoint)
            return ((ProceedingJoinPoint) context.joinPoint()).proceed();

        return null;
    }

    protected abstract void execute(final ExecutionContext context) throws Throwable;

    private AspectExecutionChain setNext(final AspectExecutionChain next) {
        return this.next = next;
    }

    public static final class Builder {

        private final Queue<AspectExecutionChain> chains;

        private Builder() {
            this.chains = new ArrayDeque<>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder add(final AspectExecutionChain chain) {
            this.chains.add(chain);
            return this;
        }

        public AspectExecutionChain build() {
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
