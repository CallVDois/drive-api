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
        List<GetFolderOutput.File> files,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt) {

    public static GetFolderOutput from(final Folder folder, List<com.callv2.drive.domain.file.File> files) {

        final var subFolders = folder.getSubFolders() != null
                ? folder.getSubFolders()
                        .stream()
                        .map(GetFolderOutput.SubFolder::from)
                        .toList()
                : null;

        final var filesOutput = files != null ? files.stream().map(GetFolderOutput.File::from).toList() : null;

        return new GetFolderOutput(
                folder.getId().getValue(),
                folder.getName().value(),
                folder.getParentFolder().getValue(),
                subFolders,
                filesOutput,
                folder.getCreatedAt(),
                folder.getUpdatedAt(),
                folder.getDeletedAt());
    }

    public static record SubFolder(UUID id, String name) {

        public static GetFolderOutput.SubFolder from(com.callv2.drive.domain.folder.SubFolder subFolder) {
            return new SubFolder(subFolder.id().getValue(), subFolder.name().value());
        }

    }

    public static record File(
            UUID id,
            String name,
            Long size,
            Instant createdAt,
            Instant updatedAt) {

        public static GetFolderOutput.File from(com.callv2.drive.domain.file.File file) {
            return new GetFolderOutput.File(
                    file.getId().getValue(),
                    file.getName().value(),
                    file.getContent().size(),
                    file.getCreatedAt(),
                    file.getUpdatedAt());
        }
    }

}
