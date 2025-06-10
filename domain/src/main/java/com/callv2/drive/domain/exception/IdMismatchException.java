package com.callv2.drive.domain.exception;

import java.util.List;

import com.callv2.drive.domain.Entity;

public class IdMismatchException extends SilentDomainException {

    private IdMismatchException(
            final Class<? extends Entity<?>> entityClass,
            final String id) {
        super("[%s]'s id doesn't match.".formatted(entityClass.getSimpleName()),
                List.of(DomainException.Error
                        .with("[%s] with id [%s] doesn't match.".formatted(entityClass.getSimpleName(), id))));
    }

    public static IdMismatchException with(
            final Class<? extends Entity<?>> entityClass,
            final String id) {
        return new IdMismatchException(entityClass, id);
    }

}
