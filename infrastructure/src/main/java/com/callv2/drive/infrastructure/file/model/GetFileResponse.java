package com.callv2.drive.infrastructure.file.model;

import java.time.Instant;

public record GetFileResponse(
        String id,
        String name,
        String contentType,
        Instant createdAt,
        Instant updatedAt) {

}
