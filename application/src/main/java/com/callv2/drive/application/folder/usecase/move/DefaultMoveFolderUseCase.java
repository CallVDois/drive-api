package com.callv2.drive.application.folder.usecase.move;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.callv2.drive.domain.acl.AccessPermission;
import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultMoveFolderUseCase extends MoveFolderUseCase {

    private final AclGateway aclGateway;
    private final FolderGateway folderGateway;

    public DefaultMoveFolderUseCase(final AclGateway aclGateway, final FolderGateway folderGateway) {
        this.aclGateway = Objects.requireNonNull(aclGateway);
        this.folderGateway = Objects.requireNonNull(folderGateway);
    }

    @Override
    public void execute(final MoveFolderInput input) {

        final FolderID folderId = FolderID.of(input.id());
        final FolderID newParentFolderId = FolderID.of(input.newParentId());
        final MemberID actorId = MemberID.of(input.actorId());

        final Folder folder = findFolder(folderId, actorId);
        final Folder newParentFolder = findFolder(newParentFolderId, actorId);

        final Acl folderAcl = aclGateway
                .findByResource(Resource.folder(folder))
                .orElseThrow(() -> NotFoundException.with(Folder.class, folderId.getValue().toString()));
        checkFolderAccessPermission(folderAcl, folderId, actorId);

        final Acl parentFolderAcl = aclGateway
                .findByResource(Resource.folder(newParentFolder))
                .orElseThrow(() -> NotFoundException.with(Folder.class, newParentFolderId.getValue().toString()));
        checkParentFolderAccessPermission(parentFolderAcl, newParentFolderId, actorId);

        final Notification notification = Notification.create();
        validateMove(folder, newParentFolder, actorId, notification);

        if (!folder.getOwner().equals(actorId))
            validateSharedFolderMove(folder, newParentFolder, actorId, notification);

        if (notification.hasError())
            throw ValidationException.with("Invalid move operation", notification);

        folder.changeParentFolder(newParentFolder);

        aclGateway.update(folderAcl.inheritFrom(parentFolderAcl));

        folderGateway.update(folder);
    }

    private Folder findFolder(FolderID id, MemberID actorId) {
        return folderGateway
                .findByIdWithMemberAccess(id, actorId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, id.getValue().toString()));
    }

    private void validateSharedFolderMove(
            final Folder folder,
            final Folder newParentFolder,
            final MemberID actorId,
            final Notification notification) {

        Folder parentFolderA = folder;
        Folder parentFolderB = newParentFolder;

        do {

            Optional<Folder> parentFolderAOpt = folderGateway
                    .findByIdWithMemberAccess(parentFolderA.getParentFolder(), actorId);

            Optional<Folder> parentFolderBOpt = folderGateway
                    .findByIdWithMemberAccess(parentFolderB.getParentFolder(), actorId);

            if (parentFolderAOpt.isEmpty() && parentFolderBOpt.isEmpty()) {
                notification.append(
                        ValidationError.with(
                                "Cannot complete the move — one of the folders is outside your accessible hierarchy."));

                break;
            }

            parentFolderA = parentFolderAOpt.orElse(parentFolderA);
            parentFolderB = parentFolderBOpt.orElse(parentFolderB);

        } while (!parentFolderA.equals(parentFolderB));

    }

    private void validateMove(
            final Folder folder,
            final Folder newParentFolder,
            final MemberID actorId,
            final Notification notification) {

        if (!folder.getOwner().equals(newParentFolder.getOwner())) {
            notification
                    .append(ValidationError.with("You cannot move a folder to a drive that belongs to another owner."));
            return;
        }

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

            actualParent = findFolder(actualParent.getVirtualParentFolder(actorId), actorId);
        }

    }

    private void checkFolderAccessPermission(
            final Acl parentFolderAcl,
            final FolderID newParentFolderId,
            final MemberID actorId) {

        final AccessPermission folderEffectiveAccessPermission = parentFolderAcl
                .effectiveAccessPermission(actorId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, newParentFolderId.getValue().toString()));

        if (!folderEffectiveAccessPermission.canWrite())
            throw NotAllowedException.with("You do not have permission to move this folder.");

    }

    private void checkParentFolderAccessPermission(
            final Acl parentFolderAcl,
            final FolderID newParentFolderId,
            final MemberID actorId) {

        final AccessPermission folderEffectiveAccessPermission = parentFolderAcl
                .effectiveAccessPermission(actorId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, newParentFolderId.getValue().toString()));

        if (!folderEffectiveAccessPermission.canWrite())
            throw NotAllowedException.with("You do not have permission to move folders into the target parent folder.");

    }

}
