package com.callv2.drive.infrastructure.folder.model;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.acl.AccessPermission;

public record FolderSharingListResponse(
        UUID id,
        UUID sharedTo,
        UUID sharedBy,
        AccessPermission accessPermission,
        Instant createdAt) {

}
