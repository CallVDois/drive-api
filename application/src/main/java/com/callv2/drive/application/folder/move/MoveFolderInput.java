package com.callv2.drive.application.folder.move;

import java.util.UUID;

public record MoveFolderInput(UUID id, UUID newParentId) {

    public static MoveFolderInput with(UUID id, UUID newParentId) {
        return new MoveFolderInput(id, newParentId);
    }

}
