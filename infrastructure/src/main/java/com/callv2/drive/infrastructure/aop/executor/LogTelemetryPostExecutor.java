package com.callv2.drive.infrastructure.aop.executor;

import java.time.Duration;

import org.apache.logging.log4j.Level;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.executor.PostExecutor;

public class LogTelemetryPostExecutor extends Log4jLogger implements PostExecutor {

    private LogTelemetryPostExecutor(final Level logLevel, final Class<?> clazz) {
        super(logLevel, clazz);
    }

    public static LogTelemetryPostExecutor create(final Level logLevel) {
        return new LogTelemetryPostExecutor(logLevel, LogTelemetryPostExecutor.class);
    }

    @Override
    public void execute(final PostInvocationContext joinPoint) {
        log("<<METHOD-CALLED>>: [{}] <<EXECUTION-TIME>> [{}] ms",
                joinPoint.getSignature().toShortString(),
                Duration.between(joinPoint.getContextedAt(), joinPoint.getProceededAt()).toMillis());
    }

}
