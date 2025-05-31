package com.callv2.drive.application.folder.update.name;

import java.util.UUID;

public record UpdateFolderNameInput(
        UUID folderId,
        String name) {

}
