package com.callv2.drive.application.file.retrieve.get;

import java.util.UUID;

public record GetFileInput(UUID id) {

    public static GetFileInput from(UUID id) {
        return new GetFileInput(id);
    }

}
