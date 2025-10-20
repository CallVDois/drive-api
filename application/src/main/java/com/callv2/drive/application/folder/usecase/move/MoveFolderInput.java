package com.callv2.drive.application.folder.usecase.move;

import java.util.UUID;

public record MoveFolderInput(UUID id, UUID newParentId, UUID actorId) {

    public static MoveFolderInput with(UUID id, UUID newParentId, UUID actorId) {
        return new MoveFolderInput(id, newParentId, actorId);
    }

}
