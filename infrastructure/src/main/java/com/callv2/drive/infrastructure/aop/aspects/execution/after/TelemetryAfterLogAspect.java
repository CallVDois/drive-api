package com.callv2.drive.infrastructure.aop.aspects.execution.after;

import java.time.Duration;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.aspects.context.AfterExecutionContext;

public class TelemetryAfterLogAspect extends LogAfterExecutionAspect {

    public TelemetryAfterLogAspect(final Level logLevel) {
        super(logLevel, TelemetryAfterLogAspect.class);
    }

    @Override
    protected void execute(final AfterExecutionContext context) throws Throwable {

        final String methodSignature = context.joinPoint().getSignature().toShortString();
        final long elapsedMillis = Duration.between(context.startedAt(), context.finishedAt()).toMillis();
        final String message = String.format(
                "Method [%s] executed in %d ms.",
                methodSignature,
                elapsedMillis);

        logger().log(level(), createMessage(message));
    }
}
