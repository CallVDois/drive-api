package com.callv2.drive.infrastructure.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import com.callv2.drive.domain.exception.InternalErrorException;
import com.callv2.drive.domain.storage.StorageService;

public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(Path rootLocation) {
        this.rootLocation = Objects.requireNonNull(rootLocation).toAbsolutePath().normalize();
    }

    @Override
    public String store(final String name, final InputStream content) {
        try {

            if (content == null)
                throw new IllegalArgumentException("Failed to store empty file.");

            Path destinationFile = this.rootLocation.resolve(Paths.get(name)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath()))
                throw new IllegalArgumentException("Cannot store file outside current directory.");

            try (InputStream inputStream = content) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return destinationFile.toString();

        } catch (IOException e) {
            throw InternalErrorException.with("Failed to store file.", e);
        }
    }

    @Override
    public void delete(final String location) {

        try {

            Path filePath = Paths.get(location).normalize().toAbsolutePath();

            if (!filePath.getParent().equals(this.rootLocation.toAbsolutePath()))
                return;

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw InternalErrorException.with("Failed to delete file.", e);
        }

    }

}
