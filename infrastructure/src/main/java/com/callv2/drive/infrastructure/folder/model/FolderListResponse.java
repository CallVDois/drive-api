package com.callv2.drive.infrastructure.folder.model;

import java.util.List;
import java.util.UUID;

public record FolderListResponse(
        UUID id,
        String name,
        UUID parentFolder,
        List<SubFolder> subFolders) {

    public static record SubFolder(UUID id, String name) {

    }

}
