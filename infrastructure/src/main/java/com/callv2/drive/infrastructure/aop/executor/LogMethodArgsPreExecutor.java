package com.callv2.drive.infrastructure.aop.executor;

import java.util.Arrays;

import org.apache.logging.log4j.Level;

import com.callv2.aop.context.PreInvocationContext;
import com.callv2.aop.executor.PreExecutor;

public class LogMethodArgsPreExecutor extends Log4jLogger implements PreExecutor {

    private LogMethodArgsPreExecutor(final Level logLevel, final Class<?> clazz) {
        super(logLevel, clazz);
    }

    public static LogMethodArgsPreExecutor create(final Level logLevel) {
        return new LogMethodArgsPreExecutor(logLevel, LogMethodArgsPreExecutor.class);
    }

    @Override
    public void execute(final PreInvocationContext joinPoint) {

        log("<<METHOD-CALLED>> [{}] <<ARGS>> [{}]",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }

}
