package com.callv2.drive.application.folder.create;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderName;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateFolderUseCase extends CreateFolderUseCase {

    private final FolderGateway folderGateway;

    public DefaultCreateFolderUseCase(final FolderGateway folderGateway) {
        this.folderGateway = folderGateway;
    }

    @Override
    public CreateFolderOutput execute(final CreateFolderInput input) {
        final Folder parentFolder = folderGateway
                .findById(FolderID.of(input.parentFolderId()))
                .orElseThrow(() -> NotFoundException.with(
                        Folder.class,
                        "Parent folder with id %s not found".formatted(input.parentFolderId().toString())));

        return CreateFolderOutput.from(createFolder(FolderName.of(input.name()), parentFolder));
    }

    private Folder createFolder(final FolderName name, final Folder parentFolder) {
        final Notification notification = Notification.create();
        if (parentFolder.getSubFolders().stream().anyMatch(subFolder -> subFolder.name().equals(name)))
            notification.append(Error.with("Folder with the same name already exists"));

        final Folder folder = notification.valdiate(() -> Folder.create(name, parentFolder));

        if (notification.hasError())
            throw ValidationException.with("Could not create Aggregate Folder", notification);

        folderGateway.update(parentFolder);
        return folderGateway.create(folder);
    }

}
