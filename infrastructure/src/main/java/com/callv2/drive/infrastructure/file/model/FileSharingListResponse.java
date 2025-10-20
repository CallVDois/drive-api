package com.callv2.drive.infrastructure.file.model;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.acl.AccessPermission;

public record FileSharingListResponse(
        UUID id,
        UUID sharedTo,
        UUID sharedBy,
        AccessPermission accessPermission,
        Instant createdAt) {

}
