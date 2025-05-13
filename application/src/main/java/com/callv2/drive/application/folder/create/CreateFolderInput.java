package com.callv2.drive.application.folder.create;

import java.util.UUID;

public record CreateFolderInput(String ownerId, String name, UUID parentFolderId) {

    public static CreateFolderInput from(String ownerdId, String name, UUID parentFolderId) {
        return new CreateFolderInput(ownerdId, name, parentFolderId);
    }

}
