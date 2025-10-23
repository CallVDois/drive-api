package com.callv2.drive.application.file.usecase.sharing.remove;

import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.file.FileSharingID;
import com.callv2.drive.domain.member.MemberID;

public class DefaultRemoveFileSharingUseCase extends RemoveFileSharingUseCase {

    private final EventDispatcher eventDispatcher;

    private final FileGateway fileGateway;
    private final AclGateway aclGateway;

    public DefaultRemoveFileSharingUseCase(
            final EventDispatcher eventDispatcher,
            final FileGateway fileGateway,
            final AclGateway aclGateway) {
        this.eventDispatcher = eventDispatcher;
        this.fileGateway = fileGateway;
        this.aclGateway = aclGateway;
    }

    @Override
    public void execute(final RemoveFileSharingInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final MemberID revoker = MemberID.of(input.revoker());
        final FileSharingID fileSharing = FileSharingID.of(input.sharingId());

        final File file = fileGateway
                .findByIdWithMemberAccess(fileId, revoker)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        final Acl acl = aclGateway.findByResource(Resource.file(file))
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        file.getSharing(fileSharing)
                .ifPresent(sharing -> {

                    acl.revokeAccess(revoker, sharing.getSharedTo());
                    file.unshare(revoker, fileSharing);

                    aclGateway.update(acl);
                    fileGateway.update(file);

                    eventDispatcher.notify(acl);
                    eventDispatcher.notify(file);

                });

    }

}
