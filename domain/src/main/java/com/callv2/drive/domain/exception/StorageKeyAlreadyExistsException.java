package com.callv2.drive.domain.exception;

import java.util.List;

public class StorageKeyAlreadyExistsException extends SilentDomainException {

    private StorageKeyAlreadyExistsException(final String key) {
        super("storage key [%s] already exists.".formatted(key),
                List.of(DomainException.Error.with("storage key [%s] already exists.".formatted(key))));
    }

    public static StorageKeyAlreadyExistsException with(final String key) {
        return new StorageKeyAlreadyExistsException(key);
    }

}
