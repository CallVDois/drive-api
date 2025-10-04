package com.callv2.drive.application.file.permissions.grant;

import java.util.Objects;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultGrantFilePermissionUseCase extends GrantFilePermissionUseCase {

    private final EventDispatcher eventDispatcher;

    private final AclGateway aclGateway;
    private final FileGateway fileGateway;

    public DefaultGrantFilePermissionUseCase(
            final EventDispatcher eventDispatcher,
            final AclGateway aclGateway,
            final FileGateway fileGateway) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.aclGateway = Objects.requireNonNull(aclGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public void execute(final GrantFilePermissionInput input) {

        final MemberID granterId = MemberID.of(input.granter());
        final MemberID granteeId = MemberID.of(input.grantee());

        final FileID fileId = fileGateway
                .findByIdWithMemberAccess(FileID.of(input.fileId()), granterId)
                .map(File::getId)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        final Acl acl = this.aclGateway
                .findByResource(Resource.file(fileId))
                .orElseThrow(() -> NotFoundException.with(File.class, fileId.getStringValue()));

        final Notification notification = Notification.create();

        notification.validate(() -> input
                .accessPermission()
                .ifPresent(ap -> acl.grantAccess(granterId, granteeId, ap)));

        notification.validate(() -> input
                .sharePermission()
                .ifPresent(sp -> acl.grantShare(granterId, granteeId, sp)));

        if (notification.hasError())
            throw ValidationException.with("Permission validation failed", notification);

        this.eventDispatcher.notify(this.aclGateway.update(acl));

    }

}
