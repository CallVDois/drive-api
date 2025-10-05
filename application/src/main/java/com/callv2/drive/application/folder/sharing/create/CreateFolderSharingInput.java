package com.callv2.drive.application.folder.sharing.create;

import java.util.Optional;
import java.util.UUID;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.SharePermission;

public record CreateFolderSharingInput(
        UUID folderId,
        UUID granter,
        UUID grantee,
        Optional<AccessPermission> accessPermission,
        Optional<SharePermission> sharePermission) {

    public static CreateFolderSharingInput with(
            final UUID folderId,
            final UUID granter,
            final UUID grantee,
            final AccessPermission accessPermission,
            final SharePermission sharePermission) {
        return new CreateFolderSharingInput(
                folderId,
                granter,
                grantee,
                Optional.ofNullable(accessPermission),
                Optional.ofNullable(sharePermission));
    }

}
