package com.callv2.drive.domain.exception;

import java.util.List;

public class VerboseDomainException extends DomainException {

    private static final boolean VERBOSE = true;

    protected VerboseDomainException(String message, List<DomainException.Error> errors, Throwable cause) {
        super(message, errors, cause, VERBOSE);
    }

}
