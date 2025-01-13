package com.callv2.drive.infrastructure.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(Path rootLocation) {
        this.rootLocation = Objects.requireNonNull(rootLocation).toAbsolutePath().normalize();
    }

    @Override
    public String store(final String name, final InputStream content) {
        try {
            if (content == null)
                throw new StorageException("Failed to store empty file.");

            Path destinationFile = this.rootLocation.resolve(Paths.get(name)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath()))
                throw new StorageException("Cannot store file outside current directory.");

            try (InputStream inputStream = content) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return destinationFile.toString();

        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
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
            throw new StorageException("Failed to delete file.", e);
        }

    }

    // @Override
    // public Content load(String name) {
    // final Path filePath = this.rootLocation.resolve(name).normalize();

    // if (!Files.exists(filePath))
    // throw NotFoundException.with(File.class);

    // final var size = filePath.toFile().length();

    // try {
    // return Content.of(new FileInputStream(filePath.toFile()), size);
    // } catch (Exception e) {
    // throw InternalErrorException.with("Error on process File Content", e);
    // }
    // }

}
