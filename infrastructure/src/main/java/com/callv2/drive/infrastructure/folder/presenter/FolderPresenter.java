package com.callv2.drive.infrastructure.folder.presenter;

import java.util.stream.Collectors;

import com.callv2.drive.application.folder.create.CreateFolderOutput;
import com.callv2.drive.application.folder.retrieve.get.GetFolderOutput;
import com.callv2.drive.application.folder.retrieve.get.root.GetRootFolderOutput;
import com.callv2.drive.application.folder.retrieve.list.FolderListOutput;
import com.callv2.drive.infrastructure.folder.model.CreateFolderResponse;
import com.callv2.drive.infrastructure.folder.model.FolderListResponse;
import com.callv2.drive.infrastructure.folder.model.GetFolderResponse;

public interface FolderPresenter {

    static CreateFolderResponse present(final CreateFolderOutput output) {
        return new CreateFolderResponse(output.id());
    }

    static GetFolderResponse present(final GetFolderOutput output) {
        return new GetFolderResponse(
                output.id(),
                output.name(),
                output.isRootFolder(),
                output.parentFolder(),
                output.subFolders().stream()
                        .map(FolderPresenter::present)
                        .collect(Collectors.toSet()),
                output.files()
                        .stream()
                        .map(FolderPresenter::present)
                        .collect(Collectors.toSet()),
                output.ownerId(),
                output.createdAt(),
                output.updatedAt());
    }

    static GetFolderResponse.SubFolder present(final GetFolderOutput.SubFolder subFolder) {
        return new GetFolderResponse.SubFolder(subFolder.id(), subFolder.name());
    }

    static GetFolderResponse.File present(final GetFolderOutput.File file) {
        return new GetFolderResponse.File(
                file.id(),
                file.name(),
                file.size(),
                file.createdAt(),
                file.updatedAt());
    }

    static GetFolderResponse present(final GetRootFolderOutput output) {
        return new GetFolderResponse(
                output.id(),
                output.name(),
                true,
                null,
                output
                        .subFolders()
                        .stream()
                        .map(FolderPresenter::present)
                        .collect(Collectors.toSet()),
                output.files()
                        .stream()
                        .map(FolderPresenter::present)
                        .collect(Collectors.toSet()),
                output.ownerId(),
                output.createdAt(),
                output.updatedAt());
    }

    static GetFolderResponse.SubFolder present(final GetRootFolderOutput.SubFolder subFolder) {
        return new GetFolderResponse.SubFolder(subFolder.id(), subFolder.name());
    }

    static GetFolderResponse.File present(final GetRootFolderOutput.File subFolder) {
        return new GetFolderResponse.File(
                subFolder.id(),
                subFolder.name(),
                subFolder.size(),
                subFolder.createdAt(),
                subFolder.updatedAt());
    }

    static GetFolderResponse.File present(final GetFolderResponse.File file) {
        return new GetFolderResponse.File(
                file.id(),
                file.name(),
                file.size(),
                file.createdAt(),
                file.updatedAt());
    }

    static FolderListResponse present(final FolderListOutput output) {
        return new FolderListResponse(
                output.id(),
                output.name(),
                output.parentFolder());
    }

}
