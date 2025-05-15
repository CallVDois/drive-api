package com.callv2.drive.infrastructure.logging.domain;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;

import com.callv2.drive.infrastructure.aop.aspects.AbstractAroundAspectHandler;
import com.callv2.drive.infrastructure.aop.aspects.chain.AspectAfterExecutionChain;
import com.callv2.drive.infrastructure.aop.aspects.chain.AspectBeforeExecutionChain;
import com.callv2.drive.infrastructure.aop.aspects.chain.AspectExecutionErrorChain;

public class FolderLog extends AbstractAroundAspectHandler {

    public FolderLog(
            final AspectBeforeExecutionChain beforeChain,
            final AspectAfterExecutionChain afterChain,
            final AspectExecutionErrorChain errorChain) {
        super(beforeChain, afterChain, errorChain);
    }

    @Override
    @Around("execution(* com.callv2.drive.domain.folder..*(..))")
    public Object handle(JoinPoint joinPoint) throws Throwable {
        return this.defaultHandle(joinPoint);
    }

}
