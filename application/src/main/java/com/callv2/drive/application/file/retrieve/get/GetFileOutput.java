package com.callv2.drive.application.file.retrieve.get;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.file.File;

public record GetFileOutput(
        UUID id,
        String name,
        String contentType,
        Long contentSize,
        Instant createdAt,
        Instant updatedAt) {

    public static GetFileOutput from(final File aFile) {
        return new GetFileOutput(
                aFile.getId().getValue(),
                aFile.getName().value(),
                aFile.getContent().type(),
                aFile.getContent().size(),
                aFile.getCreatedAt(),
                aFile.getUpdatedAt());
    }

}
