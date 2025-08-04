package com.callv2.drive.infrastructure.aop.executor;

import java.util.Arrays;

import org.apache.logging.log4j.Level;

import com.callv2.aop.context.PreInvocationContext;
import com.callv2.aop.executor.PreExecutor;

public class LogMethodArgsPreExecutor implements PreExecutor {

    private final Log4jLogger logger;

    public LogMethodArgsPreExecutor(final Level logLevel) {
        this.logger = new Log4jLogger(logLevel, LogTelemetryPostExecutor.class);
    }

    public LogMethodArgsPreExecutor(final Level logLevel, final Class<?> clazz) {
        this.logger = new Log4jLogger(logLevel, clazz);
    }

    @Override
    public void execute(final PreInvocationContext joinPoint) {
        final var args = joinPoint.getArgs();
        logger.log("<<METHOD-CALLED>> [{}] <<ARGS>> count:[{}] args: [{}]",
                joinPoint.getSignature(),
                args.length,
                Arrays.toString(args));
    }

}
