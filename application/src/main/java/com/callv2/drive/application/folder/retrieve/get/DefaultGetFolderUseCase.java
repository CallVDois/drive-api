package com.callv2.drive.application.folder.retrieve.get;

import java.util.Objects;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;

public class DefaultGetFolderUseCase extends GetFolderUseCase {

    private final AclGateway aclGateway;
    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;

    public DefaultGetFolderUseCase(
            final AclGateway aclGateway,
            final FolderGateway folderGateway,
            final FileGateway fileGateway) {
        this.aclGateway = Objects.requireNonNull(aclGateway);
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetFolderOutput execute(final GetFolderInput input) {

        final MemberID actorId = MemberID.of(input.actorId());
        final FolderID folderId = FolderID.of(input.folderId());

        // final Acl folderAcl = aclGateway
        //         .findByResource(Resource.folder(folderId))
        //         .orElseThrow();

        // final AccessPermission accessPermission = folderAcl
        //         .effectiveAccessPermission(actorId)
        //         .orElseThrow(); // TODO exception for notFound

        // if (!accessPermission.canRead())
        //     throw new RuntimeException(); // TODO exception for no permission

        final Folder folder = folderGateway
                .findByIdWithMemberAccess(folderId, actorId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        return GetFolderOutput
                .from(
                        folder,
                        folderGateway.findByParentFolderId(folder.getId()),
                        fileGateway.findByFolder(folder.getId()));
    }

}
