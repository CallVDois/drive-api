package com.callv2.drive.infrastructure.file.model;

import java.time.Instant;
import java.util.UUID;

public record GetFileResponse(
        UUID id,
        UUID ownerId,
        UUID folderId,
        String name,
        String contentType,
        Long contentSize,
        UUID createdBy,
        Instant createdAt,
        UUID updatedBy,
        Instant updatedAt,
        UUID deletedBy,
        Instant deletedAt) {

}
