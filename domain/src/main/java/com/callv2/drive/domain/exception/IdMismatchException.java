package com.callv2.drive.domain.exception;

import java.util.List;

import com.callv2.drive.domain.validation.Error;

public class IdMismatchException extends DomainException {

    private IdMismatchException(final String aMessage, final List<Error> errors) {
        super(aMessage, errors);
    }

    public static IdMismatchException with(final String aMessage, final List<Error> errors) {
        return new IdMismatchException(aMessage, errors);
    }

}
