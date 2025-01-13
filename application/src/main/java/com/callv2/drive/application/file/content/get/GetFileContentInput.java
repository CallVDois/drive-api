package com.callv2.drive.application.file.content.get;

import java.util.UUID;

public record GetFileContentInput(UUID fileId) {

    public static GetFileContentInput with(final UUID fileId) {
        return new GetFileContentInput(fileId);
    }

}
