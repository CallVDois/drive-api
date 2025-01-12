package com.callv2.drive.infrastructure.storage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import com.callv2.drive.domain.exception.InternalErrorException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;

public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final int BUFFER_SIZE = 8192;

    public FileSystemStorageService(Path rootLocation) {
        this.rootLocation = Objects.requireNonNull(rootLocation).toAbsolutePath().normalize();
    }

    @Override
    public void store(final String name, final InputStream content) {
        try {
            if (content == null)
                throw new StorageException("Failed to store empty file.");

            Path destinationFile = this.rootLocation.resolve(Paths.get(name)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath()))
                throw new StorageException("Cannot store file outside current directory.");

            try (InputStream inputStream = content) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public void delete(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public InputStream load(String name) {
        final Path filePath = this.rootLocation.resolve(name).normalize();

        if (!Files.exists(filePath))
            throw NotFoundException.with(File.class);

        try (final var aham = new FileInputStream(filePath.toFile())) {
            return new BufferedInputStream(new FileInputStream(filePath.toFile()));
        } catch (Exception e) {
            throw InternalErrorException.with("Error on process File Content", e);
        }
    }

    @Override
    public int writeTo(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, bytesRead);

            outputStream.flush();
            return bytesRead;
        } catch (Exception e) {
            throw InternalErrorException.with("Error on process File Content", e);
        }
    }
    /*
     * private int load(final String name, final OutputStream output) {
     * try {
     * final Path filePath = this.rootLocation.resolve(name).normalize();
     * 
     * if (!Files.exists(filePath))
     * throw NotFoundException.with(File.class);
     * 
     * try (InputStream inputStream = new BufferedInputStream(new
     * FileInputStream(filePath.toFile()))) {
     * byte[] buffer = new byte[BUFFER_SIZE];
     * int bytesRead;
     * while ((bytesRead = inputStream.read(buffer)) != -1)
     * output.write(buffer, 0, bytesRead);
     * 
     * output.flush();
     * return bytesRead;
     * }
     * } catch (Exception e) {
     * throw InternalErrorException.with("Error on process File Content", e);
     * }
     * }
     * 
     * private InputStream getFileInputStream(final String name) throws IOException
     * {
     * final Path filePath = this.rootLocation.resolve(name).normalize();
     * 
     * if (!Files.exists(filePath))
     * throw NotFoundException.with(File.class);
     * 
     * return new BufferedInputStream(new FileInputStream(filePath.toFile()));
     * }
     * 
     * private void writeToOutputStream(final InputStream inputStream, final
     * OutputStream output) throws IOException {
     * byte[] buffer = new byte[BUFFER_SIZE];
     * int bytesRead;
     * while ((bytesRead = inputStream.read(buffer)) != -1) {
     * output.write(buffer, 0, bytesRead);
     * }
     * output.flush();
     * }
     */
}
