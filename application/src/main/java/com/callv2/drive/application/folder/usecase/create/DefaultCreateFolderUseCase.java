package com.callv2.drive.application.folder.usecase.create;

import com.callv2.drive.application.folder.service.FolderProvisioningApplicationService;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.folder.valueobject.FolderName;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

public class DefaultCreateFolderUseCase extends CreateFolderUseCase {

    private final FolderProvisioningApplicationService folderProvisioningApplicationService;

    private final MemberGateway memberGateway;

    public DefaultCreateFolderUseCase(
            final FolderProvisioningApplicationService folderProvisioningApplicationService,
            final MemberGateway memberGateway) {
        this.folderProvisioningApplicationService = folderProvisioningApplicationService;
        this.memberGateway = memberGateway;
    }

    @Override
    public CreateFolderOutput execute(final CreateFolderInput input) {

        final MemberID creatorId = MemberID.of(input.creatorId());
        if (!memberGateway.existsById(creatorId))
            throw NotFoundException.with(Member.class, input.creatorId().toString());

        final Folder folder = folderProvisioningApplicationService.folder(
                FolderID.of(input.parentFolderId()),
                creatorId,
                FolderName.of(input.name()));

        return CreateFolderOutput.from(folder);
    }

}
