package com.callv2.drive.domain.exception;

import static java.util.Objects.nonNull;

import java.util.List;

public class NotAllowedException extends SilentDomainException {

    private static final String DEFAULT_MESSAGE = "The requested action is not allowed.";

    private NotAllowedException(final String message, final List<String> errors) {
        super(
                message,
                nonNull(errors) ? errors.stream().map(DomainException.Error::with).toList() : List.of());
    }

    public static NotAllowedException with(final String error) {
        final List<String> list = error == null ? List.of() : List.of(error);
        return new NotAllowedException(DEFAULT_MESSAGE, list);
    }

    public static NotAllowedException with(final String message, final String error) {
        final List<String> list = error == null ? List.of() : List.of(error);
        return new NotAllowedException(message, list);
    }

}
