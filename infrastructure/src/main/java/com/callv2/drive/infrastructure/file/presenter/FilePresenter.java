package com.callv2.drive.infrastructure.file.presenter;

import com.callv2.drive.application.file.create.CreateFileOutput;
import com.callv2.drive.application.file.retrieve.get.GetFileOutput;
import com.callv2.drive.application.file.retrieve.list.FileListOutput;
import com.callv2.drive.infrastructure.file.model.CreateFileResponse;
import com.callv2.drive.infrastructure.file.model.FileListResponse;
import com.callv2.drive.infrastructure.file.model.GetFileResponse;

public interface FilePresenter {

    static CreateFileResponse present(final CreateFileOutput output) {
        return new CreateFileResponse(output.id().getValue());
    }

    static GetFileResponse present(final GetFileOutput output) {
        return new GetFileResponse(
                output.id(),
                output.name(),
                output.contentType(),
                output.contentSize(),
                output.createdAt(),
                output.updatedAt());
    }

    static FileListResponse present(final FileListOutput output) {
        return new FileListResponse(
                output.id(),
                output.name(),
                output.folder(),
                output.createdAt(),
                output.updatedAt());
    }

}
