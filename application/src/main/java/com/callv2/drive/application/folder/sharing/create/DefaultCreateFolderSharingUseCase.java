package com.callv2.drive.application.folder.sharing.create;

import java.util.stream.Collectors;

import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.folder.service.FolderCreationService;
import com.callv2.drive.domain.folder.service.result.FolderCreationResult;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateFolderSharingUseCase extends CreateFolderSharingUseCase {

    private final EventDispatcher eventDispatcher;

    private final MemberGateway memberGateway;
    private final AclGateway aclGateway;
    private final FolderGateway folderGateway;

    public DefaultCreateFolderSharingUseCase(
            final EventDispatcher eventDispatcher,
            final MemberGateway memberGateway,
            final AclGateway aclGateway,
            final FolderGateway folderGateway) {
        this.eventDispatcher = eventDispatcher;
        this.memberGateway = memberGateway;
        this.aclGateway = aclGateway;
        this.folderGateway = folderGateway;
    }

    @Override
    public void execute(final CreateFolderSharingInput input) {

        final MemberID granterId = memberGateway.findById(MemberID.of(input.granter()))
                .map(Member::getId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.granter().toString()));

        final MemberID granteeId = memberGateway.findById(MemberID.of(input.grantee()))
                .map(Member::getId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.grantee().toString()));

        final Folder folder = folderGateway
                .findByIdWithMemberAccess(FolderID.of(input.folderId()), granterId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        final Acl acl = this.aclGateway
                .findByResource(Resource.folder(folder))
                .orElseThrow(() -> NotFoundException.with(Folder.class, folder.getId().getStringValue()));

        final Notification notification = Notification.create();

        acl.grantAccess(granterId, granteeId, input.accessPermission());

        if (notification.hasError())
            throw ValidationException.with("Permission validation failed", notification);

        final Folder inboxFolder = this.folderGateway
                .findDefaultMemberSharedInbox(granteeId)
                .orElseGet(() -> provisionInboxFolder(granteeId));

        folder.share(granterId, granteeId, inboxFolder.getId());

        this.eventDispatcher.notify(this.folderGateway.update(folder));
        this.eventDispatcher.notify(this.aclGateway.update(acl));

    }

    private Folder provisionInboxFolder(final MemberID owner) {
        final Folder rootFolder = this.folderGateway
                .findMemberRootFolder(owner)
                .orElseGet(() -> provisionRootFolder(owner));

        final var parentFolderNames = this.folderGateway
                .findByParentFolderId(rootFolder.getId())
                .stream()
                .map(Folder::getName)
                .collect(Collectors.toSet());

        final FolderCreationResult folderProvisioningResult = FolderCreationService.createInboxFolder(
                owner,
                rootFolder,
                parentFolderNames);

        this.eventDispatcher.notify(this.folderGateway.create(folderProvisioningResult.folder()));
        this.eventDispatcher.notify(this.aclGateway.create(folderProvisioningResult.acl()));

        return folderProvisioningResult.folder();
    }

    private Folder provisionRootFolder(final MemberID owner) {
        final FolderCreationResult folderProvisioningResult = FolderCreationService.createRootFolder(owner);

        this.eventDispatcher.notify(this.folderGateway.create(folderProvisioningResult.folder()));
        this.eventDispatcher.notify(this.aclGateway.create(folderProvisioningResult.acl()));

        return folderProvisioningResult.folder();
    }

}
