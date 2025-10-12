package com.callv2.drive.application.folder.usecase.sharing.create;

import com.callv2.drive.application.folder.service.FolderProvisioningApplicationService;
import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateFolderSharingUseCase extends CreateFolderSharingUseCase {

    private final EventDispatcher eventDispatcher;

    private final MemberGateway memberGateway;
    private final AclGateway aclGateway;
    private final FolderGateway folderGateway;
    private final FolderProvisioningApplicationService folderProvisioningApplicationService;

    public DefaultCreateFolderSharingUseCase(
            final EventDispatcher eventDispatcher,
            final MemberGateway memberGateway,
            final AclGateway aclGateway,
            final FolderGateway folderGateway,
            final FolderProvisioningApplicationService folderProvisioningApplicationService) {
        this.eventDispatcher = eventDispatcher;
        this.memberGateway = memberGateway;
        this.aclGateway = aclGateway;
        this.folderGateway = folderGateway;
        this.folderProvisioningApplicationService = folderProvisioningApplicationService;
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
                .orElseGet(() -> folderProvisioningApplicationService.inboxFolder(granteeId));

        folder.share(granterId, granteeId, inboxFolder.getId());

        this.eventDispatcher.notify(this.folderGateway.update(folder));
        this.eventDispatcher.notify(this.aclGateway.update(acl));

    }

}
