package com.callv2.drive.infrastructure.file.model;

import java.time.Instant;
import java.util.UUID;

public record GetFileResponse(
        UUID id,
        String name,
        String contentType,
        Long contentSize,
        Instant createdAt,
        Instant updatedAt) {

}
