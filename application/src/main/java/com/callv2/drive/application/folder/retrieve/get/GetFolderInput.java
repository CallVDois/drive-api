package com.callv2.drive.application.folder.retrieve.get;

import java.util.UUID;

public record GetFolderInput(UUID folderId, UUID actorId) {

    public static GetFolderInput with(final UUID folderId, final UUID actorId) {
        return new GetFolderInput(folderId, actorId);
    }

}
