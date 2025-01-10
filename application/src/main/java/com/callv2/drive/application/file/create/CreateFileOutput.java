package com.callv2.drive.application.file.create;

import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileID;

public record CreateFileOutput(FileID id) {

    public static CreateFileOutput from(File file) {
        return new CreateFileOutput(file.getId());
    }

}
