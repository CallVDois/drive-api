package com.callv2.drive.application.file.retrieve.get;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.file.File;

public record GetFileOutput(
        UUID id,
        UUID ownerId,
        UUID folderId,
        String name,
        String contentType,
        Long contentSize,
        UUID creatorId,
        Instant createdAt,
        UUID updaterId,
        Instant updatedAt,
        UUID deleterId,
        Instant deletedAt) {

    public static GetFileOutput from(final File file) {
        return new GetFileOutput(
                file.getId().getValue(),
                file.getOwner().getValue(),
                file.getFolder().getValue(),
                file.getName().value(),
                file.getContent().type(),
                file.getContent().size(),
                file.getCreator().getValue(),
                file.getCreatedAt(),
                file.getUpdatedBy().getValue(),
                file.getUpdatedAt(),
                file.getDeletedBy() != null ? file.getDeletedBy().getValue() : null,
                file.getDeletedAt());
    }

}
