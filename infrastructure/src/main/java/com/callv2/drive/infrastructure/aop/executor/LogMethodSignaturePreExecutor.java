package com.callv2.drive.infrastructure.aop.executor;

import org.apache.logging.log4j.Level;

import com.callv2.aop.context.PreInvocationContext;
import com.callv2.aop.executor.PreExecutor;

public class LogMethodSignaturePreExecutor extends Log4jLogger implements PreExecutor {

    private LogMethodSignaturePreExecutor(final Level logLevel, final Class<?> clazz) {
        super(logLevel, clazz);
    }

    public static LogMethodSignaturePreExecutor create(final Level logLevel) {
        return new LogMethodSignaturePreExecutor(logLevel, LogMethodSignaturePreExecutor.class);
    }

    @Override
    public void execute(final PreInvocationContext joinPoint) {
        log("<<METHOD-CALLED>>: [{}]", joinPoint.getSignature().toShortString());
    }

}
