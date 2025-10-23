package com.callv2.drive.application.folder.usecase.sharing.remove;

import java.util.UUID;

public record RemoveFolderSharingInput(
        UUID folderId,
        UUID sharingId,
        UUID revoker) {

    public static RemoveFolderSharingInput of(final UUID folderId, final UUID sharingId, final UUID revoker) {
        return new RemoveFolderSharingInput(folderId, sharingId, revoker);
    }

}