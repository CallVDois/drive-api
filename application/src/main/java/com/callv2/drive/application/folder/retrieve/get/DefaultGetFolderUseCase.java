package com.callv2.drive.application.folder.retrieve.get;

import java.util.Objects;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.member.MemberID;

public class DefaultGetFolderUseCase extends GetFolderUseCase {

    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;

    public DefaultGetFolderUseCase(
            final FolderGateway folderGateway,
            final FileGateway fileGateway) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetFolderOutput execute(final GetFolderInput input) {

        final MemberID actorId = MemberID.of(input.actorId());
        final FolderID folderId = FolderID.of(input.folderId());

        final Folder folder = folderGateway
                .findByIdWithMemberAccess(folderId, actorId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        return GetFolderOutput
                .from(
                        actorId,
                        folder,
                        folderGateway.findByParentFolderIdWithMemberAccess(folder.getId(), actorId),
                        fileGateway.findAllByFolder(folder.getId()));
    }

}
