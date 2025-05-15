package com.callv2.drive.infrastructure.aop.aspects.execution.before;

import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.aspects.context.BeforeExecutionContext;

public class TelemetryBeforeLogAspect extends LogBeforeExecutionAspect {

    public TelemetryBeforeLogAspect(final Level logLevel) {
        super(logLevel, TelemetryBeforeLogAspect.class);
    }

    @Override
    protected void execute(final BeforeExecutionContext context) throws Throwable {
        logger()
                .log(
                        level(),
                        createMessage(
                                "ElapsedTimeProcessingAspectChain: "
                                        + Duration.between(context.startTime(), Instant.now()).toNanos()));
    }
}
