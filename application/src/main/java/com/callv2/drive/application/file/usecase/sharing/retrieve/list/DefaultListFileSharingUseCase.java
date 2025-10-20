package com.callv2.drive.application.file.usecase.sharing.retrieve.list;

import java.util.List;

import com.callv2.drive.domain.acl.AccessPermission;
import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.member.MemberID;

public class DefaultListFileSharingUseCase extends ListFileSharingUseCase {

    private final FileGateway fileGateway;
    private final AclGateway aclGateway;

    public DefaultListFileSharingUseCase(
            final FileGateway fileGateway,
            final AclGateway aclGateway) {
        this.fileGateway = fileGateway;
        this.aclGateway = aclGateway;
    }

    @Override
    public List<FileSharingListOutput> execute(final ListFileSharingInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final MemberID actorId = MemberID.of(input.actorId());

        final File file = fileGateway
                .findByIdWithMemberAccess(fileId, actorId)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        final Acl acl = aclGateway.findByResource(Resource.file(file))
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        final AccessPermission accessPermission = acl.effectiveAccessPermission(actorId)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        if (!accessPermission.canShare())
            throw NotAllowedException.with(
                    "Cannot list sharings: member "
                            + actorId.getStringValue()
                            + " does not have permission to share this file.",
                    "Member "
                            + actorId.getStringValue()
                            + " cannot list sharings for file "
                            + fileId.getStringValue());

        return file.getSharings()
                .stream()
                .map(sharing -> FileSharingListOutput.from(
                        sharing,
                        acl.effectiveAccessPermission(actorId).orElse(null)))
                .toList();

    }

}
