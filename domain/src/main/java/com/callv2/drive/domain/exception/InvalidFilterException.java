package com.callv2.drive.domain.exception;

import java.util.List;

public class InvalidFilterException extends SilentDomainException {

    protected InvalidFilterException(final String message, final List<Error> errors) {
        super(message, errors);
    }

}
