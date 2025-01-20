package com.callv2.drive.application.file.retrieve.list;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.file.File;

public record FileListOutput(
        UUID id,
        String name,
        UUID folder,
        Instant createdAt,
        Instant updatedAt) {

    public static FileListOutput from(final File file) {
        return new FileListOutput(
                file.getId().getValue(),
                file.getName().value(),
                file.getFolder().getValue(),
                file.getCreatedAt(),
                file.getUpdatedAt());
    }

}