package com.callv2.drive.application.file.delete;

import java.util.Objects;

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

    private final MemberGateway memberGateway;
    private final FileGateway fileGateway;
    private final EventDispatcher eventDispatcher;

    public DefaultDeleteFileUseCase(
            final MemberGateway memberGateway,
            final FileGateway fileGateway,
            final EventDispatcher eventDispatcher) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    @Override
    public void execute(final DeleteFileInput input) {
        final MemberID ownerId = MemberID.of(input.deleterId());
        final FileID fileId = FileID.of(input.fileId());

        final Member member = memberGateway.findById(ownerId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.deleterId()));

        if (!member.hasSystemAccess())
            throw NotAllowedException.with("Member does not have permission to delete files.");

        final File file = fileGateway.findById(fileId)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        eventDispatcher.notify(fileGateway.update(file.delete()));

    }

}
