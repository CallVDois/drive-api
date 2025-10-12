package com.callv2.drive.application.file.usecase.sharing.create;

import com.callv2.drive.application.file.gateway.InboxFolderProvisioningGateway;
import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

public class DefaultCreateFileSharingUseCase extends CreateFileSharingUseCase {

    private final EventDispatcher eventDispatcher;

    private final MemberGateway memberGateway;
    private final AclGateway aclGateway;
    private final FileGateway fileGateway;
    private final FolderGateway folderGateway;
    private final InboxFolderProvisioningGateway inboxFolderProvisioningGateway;

    public DefaultCreateFileSharingUseCase(
            final EventDispatcher eventDispatcher,
            final MemberGateway memberGateway,
            final AclGateway aclGateway,
            final FileGateway fileGateway,
            final FolderGateway folderGateway,
            final InboxFolderProvisioningGateway inboxFolderProvisioningGateway) {
        this.eventDispatcher = eventDispatcher;
        this.memberGateway = memberGateway;
        this.aclGateway = aclGateway;
        this.fileGateway = fileGateway;
        this.folderGateway = folderGateway;
        this.inboxFolderProvisioningGateway = inboxFolderProvisioningGateway;
    }

    @Override
    public void execute(final CreateFileSharingInput input) {

        final MemberID granterId = memberGateway.findById(MemberID.of(input.granter()))
                .map(Member::getId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.granter().toString()));

        final MemberID granteeId = memberGateway.findById(MemberID.of(input.grantee()))
                .map(Member::getId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.grantee().toString()));

        final File file = fileGateway
                .findByIdWithMemberAccess(FileID.of(input.fileId()), granterId)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        final Acl acl = this.aclGateway
                .findByResource(Resource.file(file))
                .orElseThrow(() -> NotFoundException.with(File.class, file.getId().getStringValue()));

        acl.grantAccess(granterId, granteeId, input.accessPermission());
        file.share(granterId, granteeId, retrieveSharedInbox(granteeId).getId());

        this.eventDispatcher.notify(this.fileGateway.update(file));
        this.eventDispatcher.notify(this.aclGateway.update(acl));

    }

    private Folder retrieveSharedInbox(final MemberID memberId) {
        return folderGateway.findDefaultMemberSharedInbox(memberId)
                .orElseGet(() -> this.inboxFolderProvisioningGateway.inboxFolder(memberId));
    }

}
