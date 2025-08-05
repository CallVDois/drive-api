package com.callv2.drive.infrastructure.aop.executor;

import org.apache.logging.log4j.Level;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.executor.PostExecutor;

public class LogErrorPostExecutor implements PostExecutor {

    private final Log4jLogger logger;

    public LogErrorPostExecutor(final Level logLevel) {
        this.logger = new Log4jLogger(logLevel, LogErrorPostExecutor.class);
    }

    public LogErrorPostExecutor(final Level logLevel, final Class<?> clazz) {
        this.logger = new Log4jLogger(logLevel, clazz);
    }

    @Override
    public void execute(final PostInvocationContext joinPoint) {
        if (joinPoint.getThrowable() != null)
            logger.log("<<METHOD-CALLED>> [{}] <<EXCEPTION>> [{}] <<EXCEPTION-MESSAGE>> [{}]",
                    joinPoint.getSignature().toShortString(),
                    joinPoint.getThrowable().getClass().getName(),
                    joinPoint.getThrowable().getMessage(),
                    joinPoint.getThrowable());
        else
            logger.log("<<METHOD-CALLED>> [{}] <<NO-EXCEPTION>>", joinPoint.getSignature().toShortString());
    }

}
