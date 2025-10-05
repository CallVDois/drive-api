package com.callv2.drive.application.file.sharing.create;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateFileSharingUseCase extends CreateFileSharingUseCase {

    private final EventDispatcher eventDispatcher;

    private final MemberGateway memberGateway;
    private final AclGateway aclGateway;
    private final FileGateway fileGateway;
    private final FolderGateway folderGateway;

    public DefaultCreateFileSharingUseCase(
            final EventDispatcher eventDispatcher,
            final MemberGateway memberGateway,
            final AclGateway aclGateway,
            final FileGateway fileGateway,
            final FolderGateway folderGateway) {
        this.eventDispatcher = eventDispatcher;
        this.memberGateway = memberGateway;
        this.aclGateway = aclGateway;
        this.fileGateway = fileGateway;
        this.folderGateway = folderGateway;
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
                .findByResource(Resource.file(file.getId()))
                .orElseThrow(() -> NotFoundException.with(File.class, file.getId().getStringValue()));

        final Notification notification = Notification.create();

        acl.grantAccess(granterId, granteeId, input.accessPermission());

        file.share(granterId, granteeId, retrieveSharedInbox(granteeId));

        if (notification.hasError())
            throw ValidationException.with("Permission validation failed", notification);

        this.eventDispatcher.notify(this.fileGateway.update(file));
        this.eventDispatcher.notify(this.aclGateway.update(acl));

    }

    private FolderID retrieveSharedInbox(final MemberID memberId) {
        return folderGateway.findDefaultMemberSharedInbox(memberId)
                .orElseGet(() -> folderGateway.create(createSharedInbox(memberId)))
                .getId();
    }

    private Folder createSharedInbox(final MemberID memberId) {
        final Folder inboxFolder = Folder.createInbox(memberId, retrieveMemberRootFolder(memberId));
        eventDispatcher.notify(aclGateway.create(Acl.create(Resource.folder(inboxFolder.getId()), memberId)));
        return folderGateway.create(inboxFolder);
    }

    private FolderID retrieveMemberRootFolder(final MemberID memberId) {
        return folderGateway.findMemberRootFolder(memberId)
                .orElseGet(() -> folderGateway.create(Folder.createRoot(memberId)))
                .getId();
    }

}
