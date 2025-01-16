package com.callv2.drive.infrastructure.file.model;

import java.time.Instant;
import java.util.UUID;

public record FileListResponse(
        UUID id,
        String name,
        UUID folder,
        Instant createdAt,
        Instant updatedAt) {

}
