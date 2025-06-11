package com.callv2.drive.domain.exception;

import java.util.List;

public class NotAllowedException extends SilentDomainException {

    private NotAllowedException(final List<String> actions) {
        super(
                "The requested action is not allowed.",
                actions.stream().map(DomainException.Error::with).toList());
    }

    public static NotAllowedException with(final String action) {
        final List<String> list = action == null ? List.of() : List.of(action);
        return new NotAllowedException(list);
    }
}
