package com.callv2.drive.application.file.sharing.create;

import java.util.Optional;
import java.util.UUID;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.SharePermission;

public record CreateSharingInput(
        UUID fileId,
        UUID granter,
        UUID grantee,
        Optional<AccessPermission> accessPermission,
        Optional<SharePermission> sharePermission) {

    public static CreateSharingInput with(
            final UUID fileId,
            final UUID granter,
            final UUID grantee,
            final AccessPermission accessPermission,
            final SharePermission sharePermission) {
        return new CreateSharingInput(
                fileId,
                granter,
                grantee,
                Optional.ofNullable(accessPermission),
                Optional.ofNullable(sharePermission));
    }

}
