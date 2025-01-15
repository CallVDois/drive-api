package com.callv2.drive.application.folder.create;

import java.util.UUID;

import com.callv2.drive.domain.folder.Folder;

public record CreateFolderOutput(UUID id) {

    public static CreateFolderOutput from(Folder folder) {
        return new CreateFolderOutput(folder.getId().getValue());
    }

}
