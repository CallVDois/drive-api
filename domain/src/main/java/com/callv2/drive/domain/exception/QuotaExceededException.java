package com.callv2.drive.domain.exception;

import java.util.List;

import com.callv2.drive.domain.validation.Error;

public class QuotaExceededException extends DomainException {

    private QuotaExceededException(final String aMessage, final List<Error> anErrors) {
        super(aMessage, List.copyOf(anErrors));
    }

    public static QuotaExceededException with(final String message, final Error error) {
        return new QuotaExceededException(message, List.of(error));
    }
}
