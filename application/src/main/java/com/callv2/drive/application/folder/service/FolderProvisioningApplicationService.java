package com.callv2.drive.application.folder.service;

import java.util.stream.Collectors;

import com.callv2.drive.application.file.gateway.InboxFolderProvisioningGateway;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.service.FolderCreationService;
import com.callv2.drive.domain.folder.service.result.FolderCreationResult;
import com.callv2.drive.domain.member.MemberID;

public class FolderProvisioningApplicationService implements InboxFolderProvisioningGateway {

    private final EventDispatcher eventDispatcher;
    private final FolderGateway folderGateway;
    private final AclGateway aclGateway;

    public FolderProvisioningApplicationService(
            final EventDispatcher eventDispatcher,
            final FolderGateway folderGateway,
            final AclGateway aclGateway) {
        this.eventDispatcher = eventDispatcher;
        this.folderGateway = folderGateway;
        this.aclGateway = aclGateway;
    }

    public Folder rootFolder(final MemberID owner) {

        final FolderCreationResult folderProvisioningResult = FolderCreationService.createRootFolder(owner);

        this.eventDispatcher.notify(this.folderGateway.create(folderProvisioningResult.folder()));
        this.eventDispatcher.notify(this.aclGateway.create(folderProvisioningResult.acl()));

        return folderProvisioningResult.folder();

    }

    public Folder inboxFolder(final MemberID owner) {

        final Folder rootFolder = this.folderGateway
                .findMemberRootFolder(owner)
                .orElseGet(() -> rootFolder(owner));

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

}
