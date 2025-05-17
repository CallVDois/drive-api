package com.callv2.drive.infrastructure.aop.aspects.executor;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.aspects.context.MethodInvocationContext;

public class MethodSignatureLogExecutor extends Log4jExecutor<MethodInvocationContext> {

    public MethodSignatureLogExecutor(final Level level, final Class<?> clazz) {
        super(level, clazz);
    }

    @Override
    public void execute(final MethodInvocationContext context) {
        log("<<METHOD-CALLED>>: [" + context.getMethod().toString() + "]");
    }

}
