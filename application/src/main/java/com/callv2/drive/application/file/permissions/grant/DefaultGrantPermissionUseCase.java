package com.callv2.drive.application.file.permissions.grant;

import java.util.Objects;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.access.SharePermission;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultGrantPermissionUseCase extends GrantPermissionUseCase {

    private final AclGateway aclGateway;
    private final FileGateway fileGateway;

    public DefaultGrantPermissionUseCase(final AclGateway aclGateway, final FileGateway fileGateway) {
        this.aclGateway = Objects.requireNonNull(aclGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public void execute(final GrantPermissionInput input) {

        final FileID fileId = FileID.of(input.fileId());
        final MemberID granterId = MemberID.of(input.granter());
        final MemberID granteeId = MemberID.of(input.grantee());

        final File file = fileGateway
                .findByIdWithMemberAccess(fileId, granterId)
                .orElseThrow(() -> NotFoundException.with(File.class, fileId.getStringValue()));

        final Acl acl = this.aclGateway
                .findByResource(Resource.file(fileId))
                .orElseThrow(() -> NotFoundException.with(File.class, fileId.getStringValue()));

        final SharePermission sharePermission = acl
                .effectiveSharePermission(granterId)
                .orElseThrow(() -> NotFoundException.with(File.class, fileId.getStringValue()));

        final var notification = Notification.create();

        // .ifPresent(acl -> {

        // this.aclGateway
        // .update(acl.grantEntry(granteeId, input.accessPermission(),
        // input.sharePermission()));

        // });

    }

}
