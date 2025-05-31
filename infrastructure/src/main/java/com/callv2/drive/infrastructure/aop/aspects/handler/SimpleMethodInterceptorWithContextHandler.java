package com.callv2.drive.infrastructure.aop.aspects.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.callv2.drive.infrastructure.aop.aspects.chain.MethodInvocationAspectExecutorChain;
import com.callv2.drive.infrastructure.aop.aspects.chain.PostInvocationAspectExecutorChain;
import com.callv2.drive.infrastructure.aop.aspects.context.MethodInvocationContext;
import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;
import com.callv2.drive.infrastructure.aop.aspects.context.SimpleMethodInvocationContext;

public final class SimpleMethodInterceptorWithContextHandler implements MethodInterceptor {

    private final MethodInvocationAspectExecutorChain beforeChain;
    private final PostInvocationAspectExecutorChain afterChain;
    private final PostInvocationAspectExecutorChain errorChain;

    public SimpleMethodInterceptorWithContextHandler(
            final MethodInvocationAspectExecutorChain beforeChain,
            final PostInvocationAspectExecutorChain afterChain,
            final PostInvocationAspectExecutorChain errorChain) {
        this.beforeChain = beforeChain;
        this.afterChain = afterChain;
        this.errorChain = errorChain;
    }

    @Override
    @Nullable
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {

        final MethodInvocationContext context = SimpleMethodInvocationContext.of(invocation);

        final PostInvocationContext postInvocationResult = beforeChain.execute(context);

        if (postInvocationResult.wasSuccessful())
            return afterChain.execute(postInvocationResult);
        else
            errorChain.execute(postInvocationResult);

        final Throwable throwable = postInvocationResult.getThrowable();
        if (throwable != null)
            throw throwable;

        throw new IllegalStateException("Invocation failed but no throwable was provided.");
    }

}
