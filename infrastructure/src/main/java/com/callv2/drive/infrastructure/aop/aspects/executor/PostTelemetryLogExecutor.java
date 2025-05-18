package com.callv2.drive.infrastructure.aop.aspects.executor;

import java.time.Duration;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;

public class PostTelemetryLogExecutor extends Log4jExecutor<PostInvocationContext> {

    public PostTelemetryLogExecutor(final Level logLevel, final Class<?> clazz) {
        super(logLevel, clazz);
    }

    @Override
    public void execute(final PostInvocationContext context) {
        log("<<EXECUTION-TIME>> [{}] ms <<METHOD>> [{}]",
                Duration.between(context.getContextedAt(), context.getProceededAt()).toMillis(),
                context.getMethod().toString());
    }

}
