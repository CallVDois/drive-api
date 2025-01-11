package com.callv2.drive.infrastructure.file.presenter;

import com.callv2.drive.application.file.create.CreateFileOutput;
import com.callv2.drive.infrastructure.file.model.FileResponse;

public interface FilePresenter {

    static FileResponse present(final CreateFileOutput output) {
        return new FileResponse(output.id().getValue().toString());
    }

}
