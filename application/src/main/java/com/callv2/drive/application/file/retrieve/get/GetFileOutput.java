package com.callv2.drive.application.file.retrieve.get;

import java.time.Instant;

import com.callv2.drive.domain.file.File;

public record GetFileOutput(
        String id,
        String name,
        String contentType,
        Instant createdAt,
        Instant updatedAt) {

    public static GetFileOutput from(final File aFile) {
        return new GetFileOutput(
                aFile.getId().getValue().toString(),
                aFile.getName().value(),
                aFile.getContentType(),
                aFile.getCreatedAt(),
                aFile.getUpdatedAt());
    }

}
