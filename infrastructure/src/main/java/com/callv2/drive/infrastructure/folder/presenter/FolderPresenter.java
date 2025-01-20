package com.callv2.drive.infrastructure.folder.presenter;

import com.callv2.drive.application.folder.create.CreateFolderOutput;
import com.callv2.drive.application.folder.retrieve.get.GetFolderOutput;
import com.callv2.drive.application.folder.retrieve.get.root.GetRootFolderOutput;
import com.callv2.drive.infrastructure.folder.model.CreateFolderResponse;
import com.callv2.drive.infrastructure.folder.model.GetFolderResponse;
import com.callv2.drive.infrastructure.folder.model.GetRootFolderResponse;

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

    static GetRootFolderResponse present(GetRootFolderOutput output) {
        return new GetRootFolderResponse(
                output.id(),
                output.subFolders().stream().map(FolderPresenter::present).toList(),
                output.createdAt());
    }

    static GetRootFolderResponse.SubFolder present(GetRootFolderOutput.SubFolder subFolder) {
        return new GetRootFolderResponse.SubFolder(subFolder.id(), subFolder.name());
    }

}
