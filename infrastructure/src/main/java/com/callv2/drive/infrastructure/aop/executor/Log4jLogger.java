package com.callv2.drive.infrastructure.aop.executor;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

public abstract class Log4jLogger {

    private final Logger logger;
    private final Level logLevel;

    protected Log4jLogger(final Level logLevel, final Class<?> clazz) {
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
