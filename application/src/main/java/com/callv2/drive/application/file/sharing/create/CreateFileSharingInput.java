package com.callv2.drive.application.file.sharing.create;

import java.util.UUID;

import com.callv2.drive.domain.acl.AccessPermission;

public record CreateFileSharingInput(
        UUID fileId,
        UUID granter,
        UUID grantee,
        AccessPermission accessPermission) {

    public static CreateFileSharingInput with(
            final UUID fileId,
            final UUID granter,
            final UUID grantee,
            final AccessPermission accessPermission) {
        return new CreateFileSharingInput(
                fileId,
                granter,
                grantee,
                accessPermission);
    }

}
