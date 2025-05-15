package com.callv2.drive.infrastructure.aop.aspects.execution.before;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.aspects.context.BeforeExecutionContext;

public class ArgsLogAspect extends LogBeforeExecutionAspect {

    public ArgsLogAspect(final Level logLevel) {
        super(logLevel, ArgsLogAspect.class);
    }

    @Override
    protected void execute(final BeforeExecutionContext context) throws Throwable {
        for (Object arg : context.joinPoint().getArgs())
            logger().log(level(), createMessage("Arg: " + (arg != null ? arg.toString() : "null")));
    }

}
