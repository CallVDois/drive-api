package com.callv2.drive.application.folder.create;

import java.util.Optional;

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
    public CreateFolderOutput execute(CreateFolderInput input) {

        final Optional<FolderID> parentFolderId = Optional.ofNullable(input.parentFolderId()).map(FolderID::of);

        Folder parentFolder;

        if (parentFolderId.isEmpty())
            parentFolder = folderGateway.findRoot().orElse(folderGateway.create(Folder.createRoot()));
        else
            parentFolder = folderGateway
                    .findById(parentFolderId.get())
                    .orElseThrow(() -> NotFoundException.with(
                            Folder.class,
                            "Parent fodler with id %s not found".formatted(input.parentFolderId().toString())));

        return CreateFolderOutput.from(createFolder(FolderName.of(input.name()), parentFolder));
    }

    private Folder createFolder(FolderName name, Folder parentFolder) {
        final Notification notification = Notification.create();
        final Folder folder = notification.valdiate(() -> Folder.create(name, parentFolder));

        if (parentFolder.getSubFolders().stream().anyMatch(subFolder -> subFolder.name().equals(name)))
            notification.append(Error.with("Folder with the same name already exists"));

        if (notification.hasError())
            throw ValidationException.with("Could not create Aggregate Folder", notification);

        return folderGateway.create(folder);
    }

}
