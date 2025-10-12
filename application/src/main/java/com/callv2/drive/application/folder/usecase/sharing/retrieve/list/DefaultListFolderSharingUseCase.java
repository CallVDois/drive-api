package com.callv2.drive.application.folder.usecase.sharing.retrieve.list;

import java.util.List;

import com.callv2.drive.domain.acl.AccessPermission;
import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.member.MemberID;

public class DefaultListFolderSharingUseCase extends ListFolderSharingUseCase {

    private final FolderGateway folderGateway;
    private final AclGateway aclGateway;

    public DefaultListFolderSharingUseCase(
            final FolderGateway folderGateway,
            final AclGateway aclGateway) {
        this.folderGateway = folderGateway;
        this.aclGateway = aclGateway;
    }

    @Override
    public List<FolderSharingListOutput> execute(final ListFolderSharingInput input) {

        final FolderID folderId = FolderID.of(input.folderId());
        final MemberID actorId = MemberID.of(input.actorId());

        final Folder folder = folderGateway
                .findByIdWithMemberAccess(folderId, actorId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        final Acl acl = aclGateway.findByResource(Resource.folder(folder))
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        final AccessPermission accessPermission = acl.effectiveAccessPermission(actorId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        if (!accessPermission.canShare())
            throw NotAllowedException.with(
                    "Cannot list sharings: member "
                            + actorId.getStringValue()
                            + " does not have permission to share this folder.",
                    "Member "
                            + actorId.getStringValue()
                            + " cannot list sharings for folder "
                            + folderId.getStringValue());

        return folder.getSharings()
                .stream()
                .map(sharing -> FolderSharingListOutput.from(
                        sharing,
                        acl.effectiveAccessPermission(actorId).orElse(null)))
                .toList();

    }

}
