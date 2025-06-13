package com.callv2.drive.application.folder.retrieve.get;

import java.time.Instant;
import java.util.List;
import java.util.Set;
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

    public static GetFolderOutput from(
            final Folder folder,
            final Set<Folder> subFolders,
            final List<com.callv2.drive.domain.file.File> files) {

        return new GetFolderOutput(
                folder.getId().getValue(),
                folder.getName().value(),
                folder.getParentFolder().getValue(),
                subFolders
                        .stream()
                        .map(GetFolderOutput.SubFolder::from)
                        .toList(),
                files.stream().map(GetFolderOutput.File::from).toList(),
                folder.getCreatedAt(),
                folder.getUpdatedAt(),
                folder.getDeletedAt());
    }

    public static record SubFolder(UUID id, String name) {

        public static GetFolderOutput.SubFolder from(final Folder subFolder) {
            return new SubFolder(subFolder.getId().getValue(), subFolder.getName().value());
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
