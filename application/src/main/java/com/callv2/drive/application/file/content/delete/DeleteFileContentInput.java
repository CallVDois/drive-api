package com.callv2.drive.application.file.content.delete;

import java.util.UUID;

public record DeleteFileContentInput(UUID id) {

    public static DeleteFileContentInput of(UUID id) {
        return new DeleteFileContentInput(id);
    }

}
