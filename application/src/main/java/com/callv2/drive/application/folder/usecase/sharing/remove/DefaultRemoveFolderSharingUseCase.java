package com.callv2.drive.application.folder.usecase.sharing.remove;

import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.folder.entity.FolderSharingID;
import com.callv2.drive.domain.member.MemberID;

public class DefaultRemoveFolderSharingUseCase extends RemoveFolderSharingUseCase {

    private final EventDispatcher eventDispatcher;
    private final FolderGateway folderGateway;
    private final AclGateway aclGateway;

    public DefaultRemoveFolderSharingUseCase(
            final EventDispatcher eventDispatcher,
            final FolderGateway folderGateway,
            final AclGateway aclGateway) {
        this.eventDispatcher = eventDispatcher;
        this.folderGateway = folderGateway;
        this.aclGateway = aclGateway;
    }

    @Override
    public void execute(final RemoveFolderSharingInput input) {

        final FolderID folderId = FolderID.of(input.folderId());
        final MemberID revoker = MemberID.of(input.revoker());
        final FolderSharingID folderSharing = FolderSharingID.of(input.sharingId());

        final Folder folder = folderGateway
                .findByIdWithMemberAccess(folderId, revoker)
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        final Acl acl = aclGateway.findByResource(Resource.folder(folder))
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        folder.getSharing(folderSharing)
                .ifPresent(sharing -> {

                    acl.revokeAccess(revoker, sharing.getSharedTo());
                    folder.unshare(revoker, folderSharing);

                    aclGateway.update(acl);
                    folderGateway.update(folder);

                    eventDispatcher.notify(acl);
                    eventDispatcher.notify(folder);

                });

    }

}
