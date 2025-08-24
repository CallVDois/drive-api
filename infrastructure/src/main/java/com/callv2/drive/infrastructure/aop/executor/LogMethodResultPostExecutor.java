package com.callv2.drive.infrastructure.aop.executor;

import java.util.Arrays;

import org.apache.logging.log4j.Level;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.executor.PostExecutor;

public class LogMethodResultPostExecutor extends Log4jLogger implements PostExecutor {

    private LogMethodResultPostExecutor(final Level logLevel, final Class<?> clazz) {
        super(logLevel, clazz);
    }

    public static LogMethodResultPostExecutor create(final Level logLevel) {
        return new LogMethodResultPostExecutor(logLevel, LogMethodResultPostExecutor.class);
    }

    @Override
    public void execute(final PostInvocationContext joinPoint) {

        log("<<METHOD-CALLED>> [{}] <<ARGS>> [{}] <<RESULT>> [{}]",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()),
                joinPoint.getResult());
    }

}
