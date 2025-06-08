package com.callv2.drive.domain.exception;

import java.util.List;

import com.callv2.drive.domain.validation.Error;

public class SynchronizedVersionOutdatedException extends DomainException {

    private SynchronizedVersionOutdatedException(final String aMessage, final List<Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static SynchronizedVersionOutdatedException with(final String aMessage) {
        return new SynchronizedVersionOutdatedException(aMessage, List.of());
    }

    public static SynchronizedVersionOutdatedException with(final String aMessage, final List<Error> anErrors) {
        return new SynchronizedVersionOutdatedException(aMessage, anErrors);
    }

}
