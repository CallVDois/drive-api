package com.callv2.drive.application.file.sharing.remove;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

public class DefaultRemoveFileSharingUseCase extends RemoveFileSharingUseCase {

    // private final FileGateway fileGateway;
    // private final MemberGateway memberGateway;
    // private final AclGateway aclGateway;

    @Override
    public void execute(final RemoveFileSharingInput input) {

        // final FileID fileId = FileID.of(input.fileId());
        // final MemberID revoker = MemberID.of(input.revoker());
        // final MemberID revokedMember = MemberID.of(input.revokedMember());

        // final File file = fileGateway
        // .findByIdWithMemberAccess(fileId, revoker)
        // .orElseThrow(() -> NotFoundException.with(File.class,
        // input.fileId().toString()));

        // final Acl acl = aclGateway.findByResource(Resource.file(file))
        // .orElseThrow(() -> NotFoundException.with(File.class,
        // input.fileId().toString()));

        // acl.

    }

}
