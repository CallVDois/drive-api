package com.callv2.drive.application.folder.usecase.delete;

import java.util.UUID;

public record DeleteFolderInput(UUID id, UUID actorId) {

}
