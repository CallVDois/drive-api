package com.callv2.drive.application.file.retrieve.list;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.file.File;

public record FileListOutput(
        UUID id,
        String ownerId,
        UUID folderId,
        String name,
        String contentType,
        Long contentSize,
        Instant createdAt,
        Instant updatedAt) {

    public static FileListOutput from(final File file) {
        return new FileListOutput(
                file.getId().getValue(),
                file.getOwner().getValue(),
                file.getFolder().getValue(),
                file.getName().value(),
                file.getContent().type(),
                file.getContent().size(),
                file.getCreatedAt(),
                file.getUpdatedAt());
    }

}