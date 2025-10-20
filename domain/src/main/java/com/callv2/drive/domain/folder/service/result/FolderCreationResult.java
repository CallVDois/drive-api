package com.callv2.drive.domain.folder.service.result;

import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.folder.entity.Folder;

public record FolderCreationResult(Acl acl, Folder folder) {

}
