package com.callv2.drive.application.file.usecase.delete;

import java.util.Objects;

import com.callv2.drive.domain.acl.AccessPermission;
import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

public class DefaultDeleteFileUseCase extends DeleteFileUseCase {

    private final AclGateway aclGateway;
    private final MemberGateway memberGateway;
    private final FileGateway fileGateway;
    private final EventDispatcher eventDispatcher;

    public DefaultDeleteFileUseCase(
            final AclGateway aclGateway,
            final MemberGateway memberGateway,
            final FileGateway fileGateway,
            final EventDispatcher eventDispatcher) {
        this.aclGateway = Objects.requireNonNull(aclGateway);
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    @Override
    public void execute(final DeleteFileInput input) {
        final MemberID deleterId = MemberID.of(input.deleterId());
        final FileID fileId = FileID.of(input.fileId());

        final Member deleter = memberGateway
                .findById(deleterId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.deleterId().toString()));

        if (!deleter.hasSystemAccess())
            throw NotAllowedException.with("Member does not have permission to delete files.");

        final File file = fileGateway
                .findByIdWithMemberAccess(fileId, deleterId)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        final Acl fileAcl = aclGateway
                .findByResource(Resource.file(file))
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        fileAcl.effectiveAccessPermission(deleterId)
                .filter(AccessPermission::canWrite)
                .orElseThrow(() -> NotAllowedException.with("Member does not have permission to delete this file."));

        eventDispatcher.notify(fileGateway.update(file.delete(deleterId)));

    }

}
