package com.callv2.drive.application.file.sharing.retrieve.list;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.acl.AccessPermission;
import com.callv2.drive.domain.file.FileSharing;

public record FileSharingListOutput(
        UUID id,
        UUID sharedTo,
        UUID sharedBy,
        AccessPermission accessPermission,
        Instant createdAt) {

    public static FileSharingListOutput from(final FileSharing sharing, final AccessPermission accessPermission) {
        return new FileSharingListOutput(
                sharing.getId().getValue(),
                sharing.getSharedTo().getValue(),
                sharing.getSharedBy().getValue(),
                accessPermission,
                sharing.getCreatedAt());
    }

}
