package com.callv2.drive.application.folder.retrieve.list;

import java.util.List;
import java.util.UUID;

import com.callv2.drive.domain.folder.Folder;

public record FolderListOutput(
        UUID id,
        String name,
        UUID parentFolder,
        List<SubFolder> subFolders) {

    public static FolderListOutput from(Folder folder) {
        return new FolderListOutput(
                folder.getId().getValue(),
                folder.getName().value(),
                folder.getParentFolder().getValue(),
                folder.getSubFolders().stream().map(SubFolder::from).toList());
    }

    public static record SubFolder(UUID id, String name) {

        public static FolderListOutput.SubFolder from(com.callv2.drive.domain.folder.SubFolder subFolder) {
            return new FolderListOutput.SubFolder(subFolder.id().getValue(), subFolder.name().value());
        }

    }

}
