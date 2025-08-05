package com.callv2.drive.infrastructure.aop.executor;

import org.apache.logging.log4j.Level;

import com.callv2.aop.context.PreInvocationContext;
import com.callv2.aop.executor.PreExecutor;

public class LogMethodSignaturePreExecutor implements PreExecutor {

    private final Log4jLogger logger;

    public LogMethodSignaturePreExecutor(final Level logLevel) {
        this.logger = new Log4jLogger(logLevel, LogMethodSignaturePreExecutor.class);
    }

    public LogMethodSignaturePreExecutor(final Level logLevel, final Class<?> clazz) {
        this.logger = new Log4jLogger(logLevel, clazz);
    }

    @Override
    public void execute(final PreInvocationContext joinPoint) {
        logger.log("<<METHOD-CALLED>>: [{}]", joinPoint.getSignature().toShortString());
    }

}
