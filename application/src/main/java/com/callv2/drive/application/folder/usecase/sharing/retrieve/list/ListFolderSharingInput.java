package com.callv2.drive.application.folder.usecase.sharing.retrieve.list;

import java.util.UUID;

public record ListFolderSharingInput(UUID folderId, UUID actorId) {

    public static ListFolderSharingInput of(final UUID folderId, final UUID actorId) {
        return new ListFolderSharingInput(folderId, actorId);
    }

}
