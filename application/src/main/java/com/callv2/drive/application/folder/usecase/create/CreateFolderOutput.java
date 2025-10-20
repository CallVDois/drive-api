package com.callv2.drive.application.folder.usecase.create;

import java.util.UUID;

import com.callv2.drive.domain.folder.entity.Folder;

public record CreateFolderOutput(UUID id) {

    public static CreateFolderOutput from(Folder folder) {
        return new CreateFolderOutput(folder.getId().getValue());
    }

}
