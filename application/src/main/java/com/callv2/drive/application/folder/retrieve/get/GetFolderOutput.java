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

    public static GetFolderOutput with(
            final UUID id,
            final String name,
            final UUID parentFolder,
            final List<GetFolderOutput.SubFolder> subFolders,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new GetFolderOutput(id, name, parentFolder, subFolders, createdAt, updatedAt, deletedAt);
    }

    public static record SubFolder(UUID id, String name) {

        public static GetFolderOutput.SubFolder from(com.callv2.drive.domain.folder.SubFolder subFolder) {
            return new SubFolder(subFolder.id().getValue(), subFolder.name().value());
        }

    }

}
