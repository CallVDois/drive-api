package com.callv2.drive.infrastructure.folder.model;

import java.util.UUID;

public record CreateFolderRequest(String name, UUID parentFolderId) {

}
