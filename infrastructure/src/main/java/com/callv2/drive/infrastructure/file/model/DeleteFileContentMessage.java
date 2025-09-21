package com.callv2.drive.infrastructure.file.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record DeleteFileContentMessage(Data data) implements Serializable {

    public record Data(
            UUID fileId,
            String ownerId,
            UUID folderId,
            String name,
            String storageKey,
            String type,
            Long size,
            Instant creadtedAt,
            Instant updatedAt,
            Instant deletedAt) implements Serializable {
    }

}