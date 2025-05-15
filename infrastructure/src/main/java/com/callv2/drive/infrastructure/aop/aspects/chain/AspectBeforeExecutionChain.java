package com.callv2.drive.infrastructure.aop.aspects.chain;

import java.util.ArrayDeque;
import java.util.Queue;

import org.aspectj.lang.ProceedingJoinPoint;

import com.callv2.drive.infrastructure.aop.aspects.context.BeforeExecutionContext;

public abstract class AspectBeforeExecutionChain {

    private AspectBeforeExecutionChain next;

    protected AspectBeforeExecutionChain() {
    }

    public final Object proceed(final BeforeExecutionContext context) throws Throwable {
        execute(context);

        if (next != null)
            return next.proceed(context);

        if (context.joinPoint() instanceof ProceedingJoinPoint)
            return ((ProceedingJoinPoint) context.joinPoint()).proceed();

        return null;
    }

    protected abstract void execute(final BeforeExecutionContext context) throws Throwable;

    private AspectBeforeExecutionChain setNext(final AspectBeforeExecutionChain next) {
        return this.next = next;
    }

    public static final class Builder {

        private final Queue<AspectBeforeExecutionChain> chains;

        private Builder() {
            this.chains = new ArrayDeque<>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder add(final AspectBeforeExecutionChain chain) {
            this.chains.add(chain);
            return this;
        }

        public AspectBeforeExecutionChain build() {
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
