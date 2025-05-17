package com.callv2.drive.infrastructure.logging.aspect.executor;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;

public abstract class Log4jPostExecutor extends Log4jExecutor<PostInvocationContext> {

    public Log4jPostExecutor(final Level logLevel, final Class<?> clazz) {
        super(logLevel, clazz);
    }

}
