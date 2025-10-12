package com.callv2.drive.application.file.usecase.content.get;

import java.util.UUID;

public record GetFileContentInput(UUID fileId, UUID actorId) {

    public static GetFileContentInput with(final UUID fileId, final UUID actorId) {
        return new GetFileContentInput(fileId, actorId);
    }

}
