package com.callv2.drive.infrastructure.folder.model;

import java.util.UUID;

import com.callv2.drive.domain.acl.AccessPermission;

public record ShareFolderRequest(UUID grantee, AccessPermission accessPermission) {

}
