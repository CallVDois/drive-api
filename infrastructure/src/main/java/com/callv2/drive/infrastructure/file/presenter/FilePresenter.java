package com.callv2.drive.infrastructure.file.presenter;

import com.callv2.drive.application.file.usecase.create.CreateFileOutput;
import com.callv2.drive.application.file.usecase.retrieve.get.GetFileOutput;
import com.callv2.drive.application.file.usecase.retrieve.list.FileListOutput;
import com.callv2.drive.application.file.usecase.sharing.retrieve.list.FileSharingListOutput;
import com.callv2.drive.infrastructure.file.model.CreateFileResponse;
import com.callv2.drive.infrastructure.file.model.FileListResponse;
import com.callv2.drive.infrastructure.file.model.FileSharingListResponse;
import com.callv2.drive.infrastructure.file.model.GetFileResponse;

public interface FilePresenter {

    static CreateFileResponse present(final CreateFileOutput output) {
        return new CreateFileResponse(output.id().getValue());
    }

    static GetFileResponse present(final GetFileOutput output) {
        return new GetFileResponse(
                output.id(),
                output.ownerId(),
                output.folderId(),
                output.name(),
                output.contentType(),
                output.contentSize(),
                output.creatorId(),
                output.createdAt(),
                output.updaterId(),
                output.updatedAt(),
                output.deleterId(),
                output.deletedAt());
    }

    static FileListResponse present(final FileListOutput output) {
        return new FileListResponse(
                output.id(),
                output.ownerId(),
                output.folderId(),
                output.name(),
                output.contentType(),
                output.contentSize(),
                output.createdAt(),
                output.updatedAt());
    }

    static FileSharingListResponse present(final FileSharingListOutput sharing) {
        return new FileSharingListResponse(
                sharing.id(),
                sharing.sharedTo(),
                sharing.sharedBy(),
                sharing.accessPermission(),
                sharing.createdAt());
    }

}
