package com.callv2.drive.infrastructure.aop.aspects;

import java.util.Objects;

import org.aspectj.lang.JoinPoint;

import com.callv2.drive.infrastructure.aop.aspects.chain.AspectAfterExecutionChain;
import com.callv2.drive.infrastructure.aop.aspects.chain.AspectBeforeExecutionChain;
import com.callv2.drive.infrastructure.aop.aspects.chain.AspectExecutionErrorChain;
import com.callv2.drive.infrastructure.aop.aspects.context.AfterExecutionContext;
import com.callv2.drive.infrastructure.aop.aspects.context.BeforeExecutionContext;
import com.callv2.drive.infrastructure.aop.aspects.context.ExecutionErrorContext;

public abstract class AbstractAroundAspectHandler implements AspectHandler {

    private final AspectBeforeExecutionChain beforeChain;
    private final AspectAfterExecutionChain afterChain;
    private final AspectExecutionErrorChain errorChain;

    protected AbstractAroundAspectHandler(
            final AspectBeforeExecutionChain beforeChain,
            final AspectAfterExecutionChain afterChain,
            final AspectExecutionErrorChain errorChain) {
        this.beforeChain = Objects.requireNonNull(beforeChain);
        this.afterChain = Objects.requireNonNull(afterChain);
        this.errorChain = Objects.requireNonNull(errorChain);
    }

    protected Object defaultHandle(final JoinPoint joinPoint) throws Throwable {
        final BeforeExecutionContext executionContext = BeforeExecutionContext.of(joinPoint);
        try {
            return afterChain
                    .proceed(AfterExecutionContext
                            .of(beforeChain.proceed(executionContext), executionContext));
        } catch (Throwable ex) {
            throw errorChain.proceed(ExecutionErrorContext.of(executionContext, ex));
        }
    }

}
