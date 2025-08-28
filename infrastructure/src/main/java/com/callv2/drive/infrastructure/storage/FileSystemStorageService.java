package com.callv2.drive.infrastructure.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import com.callv2.drive.domain.exception.InternalErrorException;
import com.callv2.drive.domain.exception.StorageKeyAlreadyExistsException;
import com.callv2.drive.domain.storage.StorageService;

public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(final Path rootLocation) {
        this.rootLocation = Objects.requireNonNull(rootLocation).toAbsolutePath().normalize();
    }

    @Override
    public void store(final String key, final InputStream content) {
        try {

            if (content == null)
                throw new IllegalArgumentException("Failed to store empty file.");

            final Path destinationFile = this.rootLocation.resolve(Paths.get(key)).normalize().toAbsolutePath();

            if (Files.exists(destinationFile))
                throw StorageKeyAlreadyExistsException.with(key);

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath()))
                throw new IllegalArgumentException("Cannot store file outside current directory.");

            try (InputStream inputStream = content) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (FileAlreadyExistsException e) {
            throw StorageKeyAlreadyExistsException.with(key);
        } catch (IOException e) {
            throw InternalErrorException.with("Failed to store file.", e);
        }
    }

    @Override
    public void delete(final String key) {

        try {

            final Path filePath = this.rootLocation.resolve(Paths.get(key)).normalize().toAbsolutePath();

            if (!filePath.getParent().equals(this.rootLocation.toAbsolutePath()))
                return;

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw InternalErrorException.with("Failed to delete file.", e);
        }

    }

    @Override
    public InputStream retrieve(final String key) {

        try {

            final Path filePath = this.rootLocation.resolve(Paths.get(key)).normalize().toAbsolutePath();
            return Files.newInputStream(filePath);

        } catch (IOException e) {
            throw InternalErrorException.with("Failed to retrieve file.", e);
        }

    }

}
