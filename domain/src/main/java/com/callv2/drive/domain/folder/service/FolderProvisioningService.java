package com.callv2.drive.domain.folder.service;

import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

public class FolderProvisioningService {

    private final EventDispatcher eventDispatcher;

    private final FolderGateway folderGateway;
    private final MemberGateway memberGateway;
    private final AclGateway aclGateway;

    public FolderProvisioningService(
            final EventDispatcher eventDispatcher,
            final FolderGateway folderGateway,
            final MemberGateway memberGateway,
            final AclGateway aclGateway) {
        this.eventDispatcher = eventDispatcher;
        this.folderGateway = folderGateway;
        this.memberGateway = memberGateway;
        this.aclGateway = aclGateway;
    }

    public Folder provisionRootFolder(final MemberID owner) {

        validateMemberExists(owner);

        return folderGateway
                .findMemberRootFolder(owner)
                .orElseGet(() -> createFolderAcl(Folder.createRoot(owner)));

    }

    public Folder provisionInboxFolder(final MemberID owner) {

        validateMemberExists(owner);

        return folderGateway
                .findDefaultMemberSharedInbox(owner)
                .orElseGet(() -> createFolderAcl(Folder.createInbox(owner, this.provisionRootFolder(owner).getId())));

    }

    private void validateMemberExists(final MemberID memberId) {
        if (Boolean.FALSE.equals(memberGateway.existsById(memberId)))
            throw NotFoundException.with(Member.class, memberId.getValue().toString());
    }

    private Folder createFolderAcl(final Folder folder) {
        eventDispatcher.notify(aclGateway.create(Acl.create(Resource.folder(folder), folder.getOwner())));
        return folder;
    }

}
