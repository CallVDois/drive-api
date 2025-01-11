package com.callv2.drive.infrastructure.storage;

public class StorageFileNotFoundException extends StorageException {

    private StorageFileNotFoundException(String message) {
        super(message);
    }

    private StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static StorageFileNotFoundException notFound(String name) {
        return new StorageFileNotFoundException("File not found: " + name);
    }

}
