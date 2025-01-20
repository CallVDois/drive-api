package com.callv2.drive.infrastructure.folder.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GetRootFolderResponse(
        UUID id,
        List<GetRootFolderResponse.SubFolder> subFolders,
        List<GetRootFolderResponse.File> files,
        Instant createdAt) {

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
