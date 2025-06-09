package com.callv2.drive.domain.exception;

import java.util.List;

import com.callv2.drive.domain.Entity;

public class AlreadyExistsException extends SilentDomainException {

    private AlreadyExistsException(
            final Class<? extends Entity<?>> entityClass,
            final String id) {
        super("[%s] already exists.".formatted(entityClass.getSimpleName()),
                List.of(DomainException.Error
                        .with("[%s] with id [%s] already exists.".formatted(entityClass.getSimpleName()))));
    }

    public static AlreadyExistsException with(
            final Class<? extends Entity<?>> entityClass,
            final String id) {
        return new AlreadyExistsException(entityClass, id);
    }

}