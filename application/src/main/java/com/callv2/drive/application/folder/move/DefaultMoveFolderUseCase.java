package com.callv2.drive.application.folder.move;

import java.util.Objects;
import java.util.Set;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultMoveFolderUseCase extends MoveFolderUseCase {

    private final FolderGateway folderGateway;

    public DefaultMoveFolderUseCase(final FolderGateway folderGateway) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
    }

    @Override
    public void execute(final MoveFolderInput input) {

        final FolderID folderId = FolderID.of(input.id());
        final FolderID newParentFolderId = FolderID.of(input.newParentId());
        final MemberID actorId = MemberID.of(input.actorId());

        final Folder folder = findFolder(folderId, actorId);
        final Folder newParentFolder = findFolder(newParentFolderId, actorId);

        final Notification notification = Notification.create();
        validateMove(folder, newParentFolder, actorId, notification);
        if (notification.hasError())
            throw ValidationException.with("Invalid move operation", notification);

        folder.changeParentFolder(newParentFolder);

        folderGateway.update(folder);
    }

    private Folder findFolder(FolderID id, MemberID actorId) {
        return folderGateway
                .findByIdWithMemberAccess(id, actorId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, id.getValue().toString()));
    }

    private void validateMove(
            final Folder folder,
            final Folder newParentFolder,
            final MemberID actorId,
            final Notification notification) {

        final Set<Folder> newParentFolderSubFolders = this.folderGateway
                .findByParentFolderIdWithMemberAccess(newParentFolder.getId(), actorId);

        if (newParentFolderSubFolders.stream().anyMatch(sf -> sf.getName().equals(folder.getName())))
            notification.append(
                    ValidationError.with("A folder with the same name already exists in the target parent folder."));

        if (folder.equals(newParentFolder))
            notification.append(ValidationError.with("Cannot move a folder into itself."));

        if (folder.isRootFolder())
            notification.append(ValidationError.with("Cannot move the root folder."));

        if (newParentFolder.isRootFolder())
            return;

        Folder actualParent = newParentFolder;
        while (!actualParent.isRootFolder()) {
            if (folder.equals(actualParent)) {
                notification.append(
                        ValidationError.with("Cannot move a folder into one of its subfolders."));
                break;
            }

            actualParent = findFolder(actualParent.getParentFolder(), actorId);
        }

    }

}
