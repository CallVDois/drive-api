package com.callv2.drive.domain.exception;

public abstract class NoStacktraceException extends RuntimeException {

    protected NoStacktraceException(final String message) {
        this(message, null);
    }

    protected NoStacktraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }

}
