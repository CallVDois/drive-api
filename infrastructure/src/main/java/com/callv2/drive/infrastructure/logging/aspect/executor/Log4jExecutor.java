package com.callv2.drive.infrastructure.logging.aspect.executor;

import org.aopalliance.intercept.Joinpoint;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

import com.callv2.drive.infrastructure.aop.aspects.executor.IdentifiableAspectExecutor;

public abstract class Log4jExecutor<J extends Joinpoint> implements IdentifiableAspectExecutor<J> {

    private final Logger logger;
    private final Level logLevel;

    public Log4jExecutor(final Level logLevel, Class<?> clazz) {
        super();
        this.logLevel = logLevel;
        this.logger = LogManager.getLogger(clazz);
    }

    public void log(final String message) {
        logger.log(logLevel, createMessage(message));
    }

    public void log(final Object message) {
        logger.log(logLevel, createMessage(message));
    }

    public void log(final String message, final Object... params) {
        logger.log(logLevel, createMessage(message, params));
    }

    private Message createMessage(final String message) {
        return logger.getMessageFactory().newMessage(message);
    }

    private Message createMessage(final Object message) {
        return logger.getMessageFactory().newMessage(message);
    }

    private Message createMessage(final String message, final Object... params) {
        return logger.getMessageFactory().newMessage(message, params);
    }

}
