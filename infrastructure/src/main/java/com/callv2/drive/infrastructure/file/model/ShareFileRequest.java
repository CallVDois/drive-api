package com.callv2.drive.infrastructure.file.model;

import java.util.UUID;

import com.callv2.drive.domain.acl.AccessPermission;

public record ShareFileRequest(UUID grantee, AccessPermission accessPermission) {

}
