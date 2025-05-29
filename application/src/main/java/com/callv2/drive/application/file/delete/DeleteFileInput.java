package com.callv2.drive.application.file.delete;

import java.util.UUID;

public record DeleteFileInput(String ownerId, UUID fileId) {

        public static DeleteFileInput of(final String ownerId, final UUID fileId) {
            return new DeleteFileInput(ownerId, fileId);
        }
}
