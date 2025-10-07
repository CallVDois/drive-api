package com.callv2.drive.application.folder.sharing.create;

import java.util.UUID;

import com.callv2.drive.domain.acl.AccessPermission;

public record CreateFolderSharingInput(
        UUID folderId,
        UUID granter,
        UUID grantee,
        AccessPermission accessPermission) {

    public static CreateFolderSharingInput with(
            final UUID folderId,
            final UUID granter,
            final UUID grantee,
            final AccessPermission accessPermission) {
        return new CreateFolderSharingInput(
                folderId,
                granter,
                grantee,
                accessPermission);
    }

}
