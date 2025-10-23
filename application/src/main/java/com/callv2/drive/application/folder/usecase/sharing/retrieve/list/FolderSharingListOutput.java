package com.callv2.drive.application.folder.usecase.sharing.retrieve.list;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.acl.AccessPermission;
import com.callv2.drive.domain.folder.entity.FolderSharing;

public record FolderSharingListOutput(
        UUID id,
        UUID sharedTo,
        UUID sharedBy,
        AccessPermission accessPermission,
        Instant createdAt) {

    public static FolderSharingListOutput from(final FolderSharing sharing, final AccessPermission accessPermission) {
        return new FolderSharingListOutput(
                sharing.getId().getValue(),
                sharing.getSharedTo().getValue(),
                sharing.getSharedBy().getValue(),
                accessPermission,
                sharing.getCreatedAt());
    }

}
