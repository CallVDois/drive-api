package com.callv2.drive.application.folder.retrieve.get.root;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.callv2.drive.domain.folder.Folder;

public record GetRootFolderOutput(
        UUID id,
        List<GetRootFolderOutput.SubFolder> subFolders,
        Instant createdAt) {

    public static GetRootFolderOutput from(final Folder folder) {
        return new GetRootFolderOutput(
                folder.getId().getValue(),
                folder.getSubFolders().stream().map(GetRootFolderOutput.SubFolder::from).toList(),
                folder.getCreatedAt());
    }

    public static record SubFolder(UUID id, String name) {

        public static GetRootFolderOutput.SubFolder from(com.callv2.drive.domain.folder.SubFolder subFolder) {
            return new SubFolder(subFolder.id().getValue(), subFolder.name().value());
        }

    }

}
