package com.callv2.drive.infrastructure.aop.context.aspects.error;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

import com.callv2.drive.infrastructure.aop.context.aspects.chain.error.AspectErrorChain;

public abstract class LogErrorAspect extends AspectErrorChain {

    private final Logger logger;

    private final Level logLevel;

    public LogErrorAspect(final Level logLevel, Class<?> clazz) {
        super();
        this.logLevel = logLevel;
        this.logger = LogManager.getLogger(clazz);
    }

    protected Level level() {
        return logLevel;
    }

    public Logger logger() {
        return logger;
    }

    protected Message createMessage(final String message) {
        return logger.getMessageFactory().newMessage(message);
    }

    protected Message createMessage(final Object message) {
        return logger.getMessageFactory().newMessage(message);
    }

    protected Message createMessage(final String message, final Object... params) {
        return logger.getMessageFactory().newMessage(message, params);
    }
}
