package com.callv2.drive.application.folder.retrieve.get.root;

import java.util.Objects;

import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.service.FolderProvisioningService;
import com.callv2.drive.domain.member.MemberID;

public class DefaultGetRootFolderUseCase extends GetRootFolderUseCase {

    private final FolderProvisioningService folderProvisioningService;
    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;

    public DefaultGetRootFolderUseCase(
            final FolderProvisioningService folderProvisioningService,
            final FolderGateway folderGateway,
            final FileGateway fileGateway) {
        this.folderProvisioningService = Objects.requireNonNull(folderProvisioningService);
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetRootFolderOutput execute(final GetRootFolderInput input) {

        final MemberID owner = MemberID.of(input.ownerId());

        final Folder folder = folderProvisioningService.provisionRootFolder(owner);

        return GetRootFolderOutput.from(
                folder,
                this.folderGateway.findByParentFolderIdWithMemberAccess(folder.getId(), owner),
                this.fileGateway.findAllByFolder(folder.getId()));

    }

}
