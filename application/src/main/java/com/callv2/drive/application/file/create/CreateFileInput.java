package com.callv2.drive.application.file.create;

import java.io.InputStream;
import java.util.UUID;

public record CreateFileInput(String ownerId, UUID folderId, String name, String contentType, InputStream content,
        long size) {

    public static CreateFileInput of(
            final String ownerId,
            final UUID folderId,
            final String name,
            final String contentType,
            final InputStream content,
            final long size) {
        return new CreateFileInput(ownerId, folderId, name, contentType, content, size);
    }

}
