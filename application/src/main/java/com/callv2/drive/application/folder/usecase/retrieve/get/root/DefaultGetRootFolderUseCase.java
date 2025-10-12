package com.callv2.drive.application.folder.usecase.retrieve.get.root;

import java.util.Objects;

import com.callv2.drive.application.folder.service.FolderProvisioningApplicationService;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.member.MemberID;

public class DefaultGetRootFolderUseCase extends GetRootFolderUseCase {

    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;
    private final FolderProvisioningApplicationService folderProvisioningApplicationService;

    public DefaultGetRootFolderUseCase(
            final FolderGateway folderGateway,
            final FileGateway fileGateway,
            final FolderProvisioningApplicationService folderProvisioningApplicationService) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.folderProvisioningApplicationService = Objects.requireNonNull(folderProvisioningApplicationService);
    }

    @Override
    public GetRootFolderOutput execute(final GetRootFolderInput input) {

        final MemberID owner = MemberID.of(input.ownerId());

        final Folder rootFolder = this.folderGateway
                .findMemberRootFolder(owner)
                .orElseGet(() -> this.folderProvisioningApplicationService.rootFolder(owner));

        return GetRootFolderOutput.from(
                rootFolder,
                this.folderGateway.findByParentFolderIdWithMemberAccess(rootFolder.getId(), owner),
                this.fileGateway.findAllByFolder(rootFolder.getId()));

    }

}
