package com.callv2.drive.application.folder.retrieve.get.root;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.callv2.drive.domain.folder.Folder;

public record GetRootFolderOutput(
        UUID id,
        List<GetRootFolderOutput.SubFolder> subFolders,
        List<GetRootFolderOutput.File> files,
        Instant createdAt) {

    public static GetRootFolderOutput from(
            final Folder folder,
            final Set<Folder> subFolders,
            final List<com.callv2.drive.domain.file.File> files) {

        final var filesOutput = files != null ? files.stream().map(GetRootFolderOutput.File::from).toList() : null;

        return new GetRootFolderOutput(
                folder.getId().getValue(),
                subFolders.stream().map(GetRootFolderOutput.SubFolder::from).toList(),
                filesOutput,
                folder.getCreatedAt());
    }

    public static record SubFolder(UUID id, String name) {

        public static GetRootFolderOutput.SubFolder from(final Folder subFolder) {
            return new SubFolder(subFolder.getId().getValue(), subFolder.getName().value());
        }

    }

    public static record File(
            UUID id,
            String name,
            Long size,
            Instant createdAt,
            Instant updatedAt) {

        public static GetRootFolderOutput.File from(com.callv2.drive.domain.file.File file) {
            return new GetRootFolderOutput.File(
                    file.getId().getValue(),
                    file.getName().value(),
                    file.getContent().size(),
                    file.getCreatedAt(),
                    file.getUpdatedAt());
        }
    }

}
