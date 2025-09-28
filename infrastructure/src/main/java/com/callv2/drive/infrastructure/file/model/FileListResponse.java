package com.callv2.drive.infrastructure.file.model;

import java.time.Instant;
import java.util.UUID;

public record FileListResponse(
        UUID id,
        UUID ownerId,
        UUID folderId,
        String name,
        String contentType,
        Long contentSize,
        Instant createdAt,
        Instant updatedAt) {

}
