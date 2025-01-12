package com.callv2.drive.domain.file;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.exception.InternalErrorException;

public record Content(InputStream inputStream, long size) implements ValueObject {

    public static Content of(InputStream inputStream, long size) {
        return new Content(inputStream, size);
    }

    public static Content of(final Path path) {
        try { // TODO validar
            return new Content(Files.newInputStream(path), Files.size(path));
        } catch (Exception e) {
            throw InternalErrorException.with("Could not create Content from Path", e);
        }
    }

}
