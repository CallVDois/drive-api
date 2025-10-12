package com.callv2.drive.application.folder.usecase.create;

import java.util.Set;

import com.callv2.drive.domain.acl.AccessPermission;
import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.folder.valueobject.FolderName;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateFolderUseCase extends CreateFolderUseCase {

    private final EventDispatcher eventDispatcher;

    private final AclGateway aclGateway;
    private final MemberGateway memberGateway;
    private final FolderGateway folderGateway;

    public DefaultCreateFolderUseCase(
            final EventDispatcher eventDispatcher,
            final AclGateway aclGateway,
            final MemberGateway memberGateway,
            final FolderGateway folderGateway) {
        this.eventDispatcher = eventDispatcher;
        this.aclGateway = aclGateway;
        this.memberGateway = memberGateway;
        this.folderGateway = folderGateway;
    }

    @Override
    public CreateFolderOutput execute(final CreateFolderInput input) {
        final MemberID creatorId = MemberID.of(input.creatorId());

        if (!memberGateway.existsById(creatorId))
            throw NotFoundException.with(Member.class, input.creatorId().toString());

        final Folder parentFolder = folderGateway
                .findByIdWithMemberAccess(FolderID.of(input.parentFolderId()), creatorId)
                .orElseThrow(() -> NotFoundException.with(
                        Folder.class,
                        "Parent folder with id %s not found".formatted(input.parentFolderId().toString())));

        return CreateFolderOutput.from(createFolder(creatorId, FolderName.of(input.name()), parentFolder));
    }

    private Folder createFolder(final MemberID creatorId, final FolderName name, final Folder parentFolder) {

        final Notification notification = Notification.create();

        final Set<Folder> subFolders = folderGateway.findByParentFolderIdWithMemberAccess(
                parentFolder.getId(),
                creatorId);

        if (subFolders.stream().anyMatch(subFolder -> subFolder.getName().equals(name)))
            notification.append(ValidationError.with("Folder with the same name already exists"));

        final Folder folder = notification
                .validate(() -> Folder.create(creatorId, parentFolder.getOwner(), name, parentFolder));

        if (notification.hasError())
            throw ValidationException.with("Could not create Aggregate Folder", notification);

        final Acl parentFolderAcl = this.aclGateway
                .findByResource(Resource.folder(parentFolder))
                .orElseThrow(() -> NotAllowedException.with("You don't have any permissions in this folder"));

        checkWriteAccessPermission(creatorId, parentFolderAcl);

        final Acl newFolderAcl = parentFolderAcl.createInherited(Resource.folder(folder));
        eventDispatcher.notify(aclGateway.create(newFolderAcl));

        return folderGateway.create(folder);
    }

    private void checkWriteAccessPermission(final MemberID memberId, final Acl folderAcl) {

        final AccessPermission folderAclPermission = folderAcl
                .effectiveAccessPermission(memberId)
                .orElseThrow(() -> NotAllowedException.with("You don't have any permissions in this folder"));

        if (!folderAclPermission.canWrite())
            throw NotAllowedException.with("You don't have permission to create files in this folder");

    }

}
