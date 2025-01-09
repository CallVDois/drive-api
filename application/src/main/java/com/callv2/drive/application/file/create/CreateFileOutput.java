package com.callv2.drive.application.file.create;

import com.callv2.drive.domain.file.File;

public record CreateFileOutput(String id) {

    public static CreateFileOutput from(File file) {
        return new CreateFileOutput(file.getId().getValue().toString());
    }

}
