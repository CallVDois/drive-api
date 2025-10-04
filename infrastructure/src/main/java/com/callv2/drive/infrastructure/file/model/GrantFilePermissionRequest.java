package com.callv2.drive.infrastructure.file.model;

import java.util.UUID;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.SharePermission;

public record GrantFilePermissionRequest(
        UUID grantee,
        AccessPermission accessPermission,
        SharePermission sharePermission) {

}
