package com.callv2.aop.executor.chain;

import org.aspectj.lang.ProceedingJoinPoint;

import com.callv2.aop.executor.Executor;

public abstract class ExecutorChain<O, J extends ProceedingJoinPoint, E extends Executor<J>> {

    private ExecutorChain<O, J, E> next;
    private final E executor;

    protected ExecutorChain(final E executor) {
        this.executor = executor;
    }

    public ExecutorChain<O, J, E> setNext(final ExecutorChain<O, J, E> executorChain) {
        return this.next = executorChain;
    }

    public final O execute(final J joinpoint) throws Throwable {

        this.executor.execute(joinpoint);

        if (next != null)
            return next.execute(joinpoint);

        return resolve(joinpoint);
    }

    protected abstract O resolve(J joinPoint) throws Throwable;

}
