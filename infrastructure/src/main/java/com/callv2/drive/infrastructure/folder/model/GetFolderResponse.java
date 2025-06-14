package com.callv2.drive.infrastructure.folder.model;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record GetFolderResponse(
        UUID id,
        String name,
        Boolean rootFolder,
        UUID parentFolder,
        Set<GetFolderResponse.SubFolder> subFolders,
        Set<GetFolderResponse.File> files,
        String ownerId,
        Instant createdAt,
        Instant updatedAt) {

    public static record SubFolder(UUID id, String name) {
    }

    public static record File(
            UUID id,
            String name,
            Long size,
            Instant createdAt,
            Instant updatedAt) {
    }

}
