package com.callv2.drive.application.folder.move;

import java.util.List;
import java.util.Objects;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.validation.Error;

public class DefaultMoveFolderUseCase extends MoveFolderUseCase {

    private final FolderGateway folderGateway;

    public DefaultMoveFolderUseCase(final FolderGateway folderGateway) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
    }

    @Override
    public void execute(MoveFolderInput input) {

        final FolderID folderId = FolderID.of(input.id());
        final FolderID newParentFolderId = FolderID.of(input.newParentId());

        final Folder folder = findFolder(folderId);
        final Folder newParentFolder = findFolder(newParentFolderId);

        if (!canMove(folder, newParentFolder))
            throw ValidationException.with("Invalid move operation", Error.with("Cannot move folder"));

        final Folder oldParentFolder = findFolder(folder.getParentFolder());
        oldParentFolder.removeSubFolder(folder);

        newParentFolder.addSubFolder(folder);
        folder.changeParentFolder(newParentFolder);

        folderGateway.updateAll(List.of(folder, newParentFolder, oldParentFolder));
    }

    private Folder findFolder(FolderID id) {
        return folderGateway
                .findById(id)
                .orElseThrow(() -> NotFoundException.with(Folder.class, id.getValue().toString()));
    }

    private boolean canMove(Folder folder, Folder newParentFolder) {

        if (newParentFolder.getSubFolders().stream().anyMatch(sf -> sf.name().equals(folder.getName())))
            return false;

        if (folder.equals(newParentFolder))
            return false;

        if (folder.isRootFolder())
            return false;

        if (newParentFolder.isRootFolder())
            return true;

        Folder actualParent = newParentFolder;
        while (!actualParent.isRootFolder()) {
            if (folder.equals(actualParent))
                return false;

            actualParent = findFolder(actualParent.getParentFolder());
        }

        return true;
    }

}
