package com.callv2.drive.infrastructure.aop.context.aspects.execution;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.context.ExecutionContext;

public class ArgsLogAspect extends LogExecutionAspect {

    public ArgsLogAspect(final Level logLevel) {
        super(logLevel, ArgsLogAspect.class);
    }

    @Override
    protected void execute(final ExecutionContext context) throws Throwable {
        for (Object arg : context.joinPoint().getArgs())
            logger().log(level(), createMessage("Arg: " + (arg != null ? arg.toString() : "null")));
    }

}
