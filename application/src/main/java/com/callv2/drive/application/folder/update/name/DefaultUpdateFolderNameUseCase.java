package com.callv2.drive.application.folder.update.name;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderName;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultUpdateFolderNameUseCase extends UpdateFolderNameUseCase {

    private final FolderGateway folderGateway;

    public DefaultUpdateFolderNameUseCase(final FolderGateway folderGateway) {
        this.folderGateway = folderGateway;
    }

    @Override
    public void execute(final UpdateFolderNameInput input) {

        final Folder folder = this.folderGateway
                .findById(FolderID.of(input.folderId()))
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        final Folder parentFolder = this.folderGateway
                .findById(folder.getParentFolder())
                .orElseThrow(
                        () -> NotFoundException.with(Folder.class, folder.getParentFolder().getValue().toString()));

        final FolderName folderName = FolderName.of(input.name());
        validateFolderName(parentFolder, folderName);

        this.folderGateway.update(folder.changeName(folderName));
    }

    private void validateFolderName(final Folder parentFolder, final FolderName name) {

        final Notification notification = Notification.create();

        if (parentFolder.getSubFolders().stream().anyMatch(subFolder -> subFolder.name().equals(name)))
            notification.append(ValidationError.with("Folder with the same name already exists"));

        if (notification.hasError())
            throw ValidationException.with("Could not update folder name", notification);
    }
}