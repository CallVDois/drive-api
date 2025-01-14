package com.callv2.drive.infrastructure.folder.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.callv2.drive.application.folder.retrieve.get.GetFolderOutput;

public record GetFolderResponse(
        UUID id,
        String name,
        UUID parentFolder,
        List<GetFolderResponse.SubFolder> subFolders,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt) {

    public static record SubFolder(UUID id, String name) {

        public static SubFolder from(GetFolderOutput.SubFolder subFolder) {
            return new SubFolder(subFolder.id(), subFolder.name());
        }

    }

}
