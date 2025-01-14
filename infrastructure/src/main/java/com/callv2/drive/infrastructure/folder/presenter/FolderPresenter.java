package com.callv2.drive.infrastructure.folder.presenter;

import com.callv2.drive.application.folder.create.CreateFolderOutput;
import com.callv2.drive.application.folder.retrieve.get.GetFolderOutput;
import com.callv2.drive.infrastructure.folder.model.CreateFolderResponse;
import com.callv2.drive.infrastructure.folder.model.GetFolderResponse;

public interface FolderPresenter {

    static CreateFolderResponse present(CreateFolderOutput output) {
        return new CreateFolderResponse(output.id());
    }

    static GetFolderResponse present(GetFolderOutput output) {
        return new GetFolderResponse(
                output.id(),
                output.name(),
                output.parentFolder(),
                output.subFolders().stream()
                        .map(GetFolderResponse.SubFolder::from)
                        .toList(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt());
    }

}
