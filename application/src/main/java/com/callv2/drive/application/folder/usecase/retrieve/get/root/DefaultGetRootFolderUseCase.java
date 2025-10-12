package com.callv2.drive.application.folder.usecase.retrieve.get.root;

import java.util.Objects;

import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.service.FolderCreationService;
import com.callv2.drive.domain.folder.service.result.FolderCreationResult;
import com.callv2.drive.domain.member.MemberID;

public class DefaultGetRootFolderUseCase extends GetRootFolderUseCase {

    private final EventDispatcher eventDispatcher;

    private final AclGateway aclGateway;
    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;

    public DefaultGetRootFolderUseCase(
            final EventDispatcher eventDispatcher,
            final AclGateway aclGateway,
            final FolderGateway folderGateway,
            final FileGateway fileGateway) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.aclGateway = Objects.requireNonNull(aclGateway);
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetRootFolderOutput execute(final GetRootFolderInput input) {

        final MemberID owner = MemberID.of(input.ownerId());

        final Folder rootFolder = this.folderGateway
                .findMemberRootFolder(owner)
                .orElseGet(() -> createRootFolder(owner));

        return GetRootFolderOutput.from(
                rootFolder,
                this.folderGateway.findByParentFolderIdWithMemberAccess(rootFolder.getId(), owner),
                this.fileGateway.findAllByFolder(rootFolder.getId()));

    }

    private Folder createRootFolder(final MemberID owner) {

        final FolderCreationResult result = FolderCreationService.createRootFolder(owner);

        this.eventDispatcher.notify(this.aclGateway.create(result.acl()));
        this.eventDispatcher.notify(this.folderGateway.create(result.folder()));

        return result.folder();

    }

}
