package com.callv2.drive.application.file.permissions.grant;

import java.util.Optional;
import java.util.UUID;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.SharePermission;

public record GrantFilePermissionInput(
        UUID fileId,
        UUID granter,
        UUID grantee,
        Optional<AccessPermission> accessPermission,
        Optional<SharePermission> sharePermission) {

    public static GrantFilePermissionInput with(
            final UUID fileId,
            final UUID granter,
            final UUID grantee,
            final AccessPermission accessPermission,
            final SharePermission sharePermission) {
        return new GrantFilePermissionInput(
                fileId,
                granter,
                grantee,
                Optional.ofNullable(accessPermission),
                Optional.ofNullable(sharePermission));
    }

}
