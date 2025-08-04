package com.callv2.drive.infrastructure.aop.executor;

import java.time.Duration;

import org.apache.logging.log4j.Level;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.executor.PostExecutor;

public class LogTelemetryPostExecutor implements PostExecutor {

    private final Log4jLogger logger;

    public LogTelemetryPostExecutor(final Level logLevel) {
        this.logger = new Log4jLogger(logLevel, LogTelemetryPostExecutor.class);
    }

    public LogTelemetryPostExecutor(final Level logLevel, final Class<?> clazz) {
        this.logger = new Log4jLogger(logLevel, clazz);
    }

    @Override
    public void execute(final PostInvocationContext joinPoint) {
        logger.log("<<EXECUTION-TIME>> [{}] ms <<METHOD>> [{}]",
                Duration.between(joinPoint.getContextedAt(), joinPoint.getProceededAt()).toMillis(),
                joinPoint.getSignature().toString());
    }

}
