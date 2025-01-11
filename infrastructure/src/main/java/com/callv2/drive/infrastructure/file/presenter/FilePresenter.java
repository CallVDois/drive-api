package com.callv2.drive.infrastructure.file.presenter;

import com.callv2.drive.application.file.create.CreateFileOutput;
import com.callv2.drive.application.file.retrieve.get.GetFileOutput;
import com.callv2.drive.infrastructure.file.model.CreateFileResponse;
import com.callv2.drive.infrastructure.file.model.GetFileResponse;

public interface FilePresenter {

    static CreateFileResponse present(final CreateFileOutput output) {
        return new CreateFileResponse(output.id().getValue().toString());
    }

    static GetFileResponse presenter(final GetFileOutput output) {
        return new GetFileResponse(
                output.id(),
                output.name(),
                output.contentType(),
                output.createdAt(),
                output.updatedAt());
    }

}
