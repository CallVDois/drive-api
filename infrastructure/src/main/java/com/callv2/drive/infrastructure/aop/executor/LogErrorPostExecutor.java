package com.callv2.drive.infrastructure.aop.executor;

import org.apache.logging.log4j.Level;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.executor.PostExecutor;

public class LogErrorPostExecutor extends Log4jLogger implements PostExecutor {

    private LogErrorPostExecutor(final Level logLevel, final Class<?> clazz) {
        super(logLevel, clazz);
    }

    public static LogErrorPostExecutor create(final Level logLevel) {
        return new LogErrorPostExecutor(logLevel, LogErrorPostExecutor.class);
    }

    @Override
    public void execute(final PostInvocationContext joinPoint) {
        if (joinPoint.getThrowable() != null)
            log("<<METHOD-CALLED>> [{}] <<EXCEPTION>> [{}] <<EXCEPTION-MESSAGE>> [{}]",
                    joinPoint.getSignature().toShortString(),
                    joinPoint.getThrowable().getClass().getName(),
                    joinPoint.getThrowable().getMessage(),
                    joinPoint.getThrowable());
        else
            log("<<METHOD-CALLED>> [{}] <<NO-EXCEPTION>>", joinPoint.getSignature().toShortString());
    }

}
