package com.callv2.drive.domain.exception;

import java.util.List;

import com.callv2.drive.domain.Entity;

public class NotFoundException extends SilentDomainException {

    private NotFoundException(
            final Class<? extends Entity<?>> entityClass,
            final String id) {
        super("[%s] not found.".formatted(entityClass.getSimpleName()),
                List.of(DomainException.Error
                        .with("[%s] with id [%s] not found.".formatted(entityClass.getSimpleName(), id))));
    }

    public static NotFoundException with(
            final Class<? extends Entity<?>> entityClass,
            final String id) {
        return new NotFoundException(entityClass, id);
    }

}
