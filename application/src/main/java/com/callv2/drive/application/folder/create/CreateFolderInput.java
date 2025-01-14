package com.callv2.drive.application.folder.create;

import java.util.UUID;

public record CreateFolderInput(String name, UUID parentFolderId) {

    public static CreateFolderInput from(String name, UUID parentFolderId) {
        return new CreateFolderInput(name, parentFolderId);
    }

}
