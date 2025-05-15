package com.callv2.drive.infrastructure.aop.aspects.execution.before;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.aspects.context.BeforeExecutionContext;

public class SignatureLogAspect extends LogBeforeExecutionAspect {

    public SignatureLogAspect(final Level logLevel) {
        super(logLevel, SignatureLogAspect.class);
    }

    @Override
    protected void execute(final BeforeExecutionContext context) throws Throwable {
        logger().log(level(),
                createMessage("SignatureLogAspect: " + context.joinPoint().getSignature().toLongString()));
    }

}
