package com.callv2.drive.infrastructure.aop.aspects.execution.error;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.aspects.context.ExecutionErrorContext;

public class StackTraceLogAspect extends LogExecutionErrorAspect {

    public StackTraceLogAspect(final Level logLevel) {
        super(logLevel, StackTraceLogAspect.class);
    }

    @Override
    protected void execute(final ExecutionErrorContext context) throws Throwable {
        logger().error(createMessage("StackTraceLogAspect: " + context.error().getMessage()), context.error());
    }

}
