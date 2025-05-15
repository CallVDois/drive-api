package com.callv2.drive.infrastructure.aop.context.aspects.execution;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.context.ExecutionContext;

public class SignatureLogAspect extends LogExecutionAspect {

    public SignatureLogAspect(final Level logLevel) {
        super(logLevel, SignatureLogAspect.class);
    }

    @Override
    protected void execute(final ExecutionContext context) throws Throwable {
        logger().log(level(),
                createMessage("SignatureLogAspect: " + context.joinPoint().getSignature().toLongString()));
    }

}
