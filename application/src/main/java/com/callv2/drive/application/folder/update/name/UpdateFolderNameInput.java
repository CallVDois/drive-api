package com.callv2.drive.application.folder.update.name;

import java.util.UUID;

public record UpdateFolderNameInput(
        UUID folderId,
        String name,
        UUID actorId) {

    public static UpdateFolderNameInput of(final UUID folderId, final String name, final UUID actorId) {
        return new UpdateFolderNameInput(folderId, name, actorId);
    }

}
