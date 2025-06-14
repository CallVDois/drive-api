package com.callv2.drive.infrastructure.folder.model;

import java.util.UUID;

public record FolderListResponse(
        UUID id,
        String name,
        UUID parentFolder) {

}
