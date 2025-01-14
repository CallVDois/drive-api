package com.callv2.drive.application.folder.retrieve.get;

import java.util.UUID;

public record GetFolderInput(UUID id) {

    public static GetFolderInput with(final UUID id) {
        return new GetFolderInput(id);
    }

}
