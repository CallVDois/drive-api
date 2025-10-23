package com.callv2.drive.application.file.usecase.retrieve.get;

import java.util.UUID;

public record GetFileInput(UUID fileId, UUID actorId) {

    public static GetFileInput from(final UUID id, final UUID actorId) {
        return new GetFileInput(id, actorId);
    }

}
