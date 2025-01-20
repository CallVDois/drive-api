package com.callv2.drive.infrastructure.folder.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GetFolderResponse(
        UUID id,
        String name,
        UUID parentFolder,
        List<GetFolderResponse.SubFolder> subFolders,
        List<GetFolderResponse.File> files,
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
