package com.callv2.drive.infrastructure.storage;

public class StorageException extends RuntimeException {

	protected StorageException(String message) {
		super(message);
	}

	protected StorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public static StorageException failedToStore(String name, Throwable cause) {
		return new StorageException("Failed to store file: " + name, cause);
	}

	public static StorageException failedToLoad(String name, Throwable cause) {
		return new StorageException("Failed to load file: " + name, cause);
	}

	public static StorageException failedToDelete(String name, Throwable cause) {
		return new StorageException("Failed to delete file: " + name, cause);
	}

}
