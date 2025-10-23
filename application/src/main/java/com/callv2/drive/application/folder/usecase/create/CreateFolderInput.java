package com.callv2.drive.application.folder.usecase.create;

import java.util.UUID;

public record CreateFolderInput(UUID creatorId, String name, UUID parentFolderId) {

    public static CreateFolderInput from(UUID creatorId, String name, UUID parentFolderId) {
        return new CreateFolderInput(creatorId, name, parentFolderId);
    }

}
