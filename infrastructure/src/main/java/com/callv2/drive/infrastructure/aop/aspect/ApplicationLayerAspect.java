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
import com.callv2.drive.infrastructure.aop.executor.LogMethodSignaturePreExecutor;
import com.callv2.drive.infrastructure.aop.executor.LogTelemetryPostExecutor;

@Aspect
@Component
public class ApplicationLayerAspect {

    private final ExecutorChainHandler chain = SimpleExecutorChainHandlerBuilder
            .create()
            .preExecutor(new LogMethodSignaturePreExecutor(Level.INFO))
            .preExecutor(new LogMethodArgsPreExecutor(Level.DEBUG))
            .postExecutor(new LogTelemetryPostExecutor(Level.INFO))
            .errorExecutor(new LogErrorPostExecutor(Level.ERROR))
            .build();

    @Around("execution(* com.callv2.drive.application..*.*(..))")
    public Object aspect(final ProceedingJoinPoint joinPoint) throws Throwable {
        return chain.handle(joinPoint);
    }

}
