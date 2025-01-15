package com.callv2.drive.application.folder.retrieve.get;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.callv2.drive.domain.folder.Folder;

public record GetFolderOutput(
        UUID id,
        String name,
        UUID parentFolder,
        List<GetFolderOutput.SubFolder> subFolders,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt) {

    public static GetFolderOutput from(final Folder folder) {

        final var subFolders = folder.getSubFolders() != null
                ? folder.getSubFolders()
                        .stream()
                        .map(GetFolderOutput.SubFolder::from)
                        .toList()
                : null;

        return new GetFolderOutput(
                folder.getId().getValue(),
                folder.getName().value(),
                folder.getParentFolder().getValue(),
                subFolders,
                folder.getCreatedAt(),
                folder.getUpdatedAt(),
                folder.getDeletedAt());
    }

    public static record SubFolder(UUID id, String name) {

        public static GetFolderOutput.SubFolder from(com.callv2.drive.domain.folder.SubFolder subFolder) {
            return new SubFolder(subFolder.id().getValue(), subFolder.name().value());
        }

    }

}
