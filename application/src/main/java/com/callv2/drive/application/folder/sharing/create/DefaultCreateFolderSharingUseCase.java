package com.callv2.drive.application.folder.sharing.create;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
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
                .findByResource(Resource.folder(folder.getId()))
                .orElseThrow(() -> NotFoundException.with(Folder.class, folder.getId().getStringValue()));

        final Notification notification = Notification.create();

        notification.validate(() -> input
                .accessPermission()
                .ifPresent(ap -> acl.grantAccess(granterId, granteeId, ap)));

        if (notification.hasError())
            throw ValidationException.with("Permission validation failed", notification);

        folder.share(granterId, granteeId, retrieveSharedInbox(granteeId).getId());

        this.folderGateway.update(folder);// this.eventDispatcher.notify();
        this.eventDispatcher.notify(this.aclGateway.update(acl));

    }

    private Folder retrieveSharedInbox(final MemberID memberId) {
        return folderGateway.findDefaultMemberSharedInbox(memberId)
                .orElseGet(() -> folderGateway.create(createSharedInbox(memberId)));
    }

    private Folder createSharedInbox(final MemberID memberId) {
        final Folder inboxFolder = Folder.createInbox(memberId, retrieveMemberRootFolder(memberId));
        eventDispatcher.notify(aclGateway.create(Acl.create(Resource.folder(inboxFolder.getId()), memberId)));
        return folderGateway.create(inboxFolder);
    }

    private FolderID retrieveMemberRootFolder(final MemberID memberId) {
        return folderGateway.findMemberRootFolder(memberId)
                .orElseGet(() -> createRootFolder(memberId))
                .getId();
    }

    private Folder createRootFolder(final MemberID memberId) {
        final Folder rootFolder = Folder.createRoot(memberId);
        eventDispatcher.notify(aclGateway.create(Acl.create(Resource.folder(rootFolder.getId()), memberId)));
        return folderGateway.create(rootFolder);
    }

}
