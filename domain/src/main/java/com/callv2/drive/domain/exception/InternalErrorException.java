package com.callv2.drive.domain.exception;

public class InternalErrorException extends NoStacktraceException {

    protected InternalErrorException(final String aMessage, final Throwable cause) {
        super(aMessage, cause);
    }

    public static InternalErrorException with(final String aMessage, final Throwable cause) {
        return new InternalErrorException(aMessage, cause);
    }

}
