package com.callv2.drive.application.file.delete;

import java.util.UUID;

public record DeleteFileInput(String deleterId, UUID fileId) {

    public static DeleteFileInput of(final String deleterId, final UUID fileId) {
        return new DeleteFileInput(deleterId, fileId);
    }

}
