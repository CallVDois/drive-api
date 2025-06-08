package com.callv2.drive.domain.exception;

import java.util.List;

import com.callv2.drive.domain.Entity;

public class SynchronizedVersionOutdatedException extends SilentDomainException {

    private SynchronizedVersionOutdatedException(
            final Class<? extends Entity<?>> entityClass,
            final Long actualEntityVersion,
            final Long outdatedVersionProvided) {
        super(
                "Outdated version provided",
                List.of(DomainException.Error.with("[%s] with actual version [%d] is outdated, but [%d] was provided."
                        .formatted(entityClass.getSimpleName(), actualEntityVersion, outdatedVersionProvided))));
    }

    public static SynchronizedVersionOutdatedException with(
            final Class<? extends Entity<?>> entityClass,
            final Long actualEntityVersion,
            final Long outdatedVersionProvided) {
        return new SynchronizedVersionOutdatedException(entityClass, actualEntityVersion, outdatedVersionProvided);
    }

}
