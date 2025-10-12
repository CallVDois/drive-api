package com.callv2.drive.application.file.usecase.sharing.create;

import java.util.stream.Collectors;

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
import com.callv2.drive.domain.folder.service.FolderCreationService;
import com.callv2.drive.domain.folder.service.result.FolderCreationResult;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

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
                .findByResource(Resource.file(file))
                .orElseThrow(() -> NotFoundException.with(File.class, file.getId().getStringValue()));

        acl.grantAccess(granterId, granteeId, input.accessPermission());
        file.share(granterId, granteeId, retrieveSharedInbox(granteeId).getId());

        this.eventDispatcher.notify(this.fileGateway.update(file));
        this.eventDispatcher.notify(this.aclGateway.update(acl));

    }

    private Folder retrieveSharedInbox(final MemberID memberId) {
        return folderGateway.findDefaultMemberSharedInbox(memberId)
                .orElseGet(() -> this.provisionInboxFolder(memberId));
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
