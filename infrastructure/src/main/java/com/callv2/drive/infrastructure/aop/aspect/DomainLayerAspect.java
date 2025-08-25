package com.callv2.drive.infrastructure.aop.aspect;

import org.apache.logging.log4j.Level;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.callv2.aop.executor.chain.handler.ExecutorChainHandler;
import com.callv2.aop.executor.chain.handler.SimpleExecutorChainHandlerBuilder;
import com.callv2.drive.infrastructure.aop.executor.LogErrorPostExecutor;
import com.callv2.drive.infrastructure.aop.executor.LogMethodArgsPreExecutor;
import com.callv2.drive.infrastructure.aop.executor.LogMethodResultPostExecutor;
import com.callv2.drive.infrastructure.aop.executor.LogMethodSignaturePreExecutor;
import com.callv2.drive.infrastructure.aop.executor.LogTelemetryPostExecutor;

@Aspect
@Component
public class DomainLayerAspect {

    private final ExecutorChainHandler chainHandler = SimpleExecutorChainHandlerBuilder
            .create()
            .preExecutor(LogMethodSignaturePreExecutor.create(Level.INFO))
            .preExecutor(LogMethodArgsPreExecutor.create(Level.DEBUG))
            .postExecutor(LogMethodResultPostExecutor.create(Level.DEBUG))
            .postExecutor(LogTelemetryPostExecutor.create(Level.INFO))
            .errorExecutor(LogErrorPostExecutor.create(Level.ERROR))
            .build();

    @Around("execution(* com.callv2.drive.domain..*.*(..))")
    public Object aspect(final ProceedingJoinPoint joinPoint) throws Throwable {
        return chainHandler.handle(joinPoint);
    }

}
