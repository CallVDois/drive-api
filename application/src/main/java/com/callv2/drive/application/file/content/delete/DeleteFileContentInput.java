package com.callv2.drive.application.file.content.delete;

import java.util.UUID;

public record DeleteFileContentInput(UUID fileId, UUID deleterId) {

    public static DeleteFileContentInput of(final UUID fileId, final UUID deleterId) {
        return new DeleteFileContentInput(fileId, deleterId);
    }

}
