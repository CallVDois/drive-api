package com.callv2.drive.application.file.usecase.sharing.retrieve.list;

import java.util.UUID;

public record ListFileSharingInput(UUID fileId, UUID actorId) {

}
