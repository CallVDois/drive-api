package com.callv2.drive.application.file.delete;

import java.util.UUID;

public record DeleteFileInput(UUID deleterId, UUID fileId) {

    public static DeleteFileInput of(final UUID deleterId, final UUID fileId) {
        return new DeleteFileInput(deleterId, fileId);
    }

}
