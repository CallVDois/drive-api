package com.callv2.drive.domain.exception;

import java.util.List;

import com.callv2.drive.domain.Entity;
import com.callv2.drive.domain.Identifier;
import com.callv2.drive.domain.validation.Error;

public class AlreadyExistsException extends DomainException {

    private AlreadyExistsException(final String message) {
        super(message, List.of(Error.with(message)));
    }

    public static AlreadyExistsException with(
            final Class<? extends Entity<? extends Identifier<?>>> entityClass) {

        final String message = String.format(
                "%s already exists",
                entityClass.getSimpleName());

        return new AlreadyExistsException(message);
    }

    public static AlreadyExistsException with(
            final Class<? extends Entity<? extends Identifier<?>>> entityClass,
            final String id) {

        final String message = String.format(
                "%s with id '%s' already exists",
                entityClass.getSimpleName(),
                id);

        return new AlreadyExistsException(message);
    }

}