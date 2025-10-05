package com.callv2.drive.application.file.retrieve.list;

import java.util.Objects;

import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.pagination.Page;

public class DefaultListFilesUseCase extends ListFilesUseCase {

    private final FileGateway fileGateway;

    public DefaultListFilesUseCase(final FileGateway fileGateway) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public Page<FileListOutput> execute(final FileListInput input) {

        final MemberID actorId = MemberID.of(input.actorId());

        return this.fileGateway
                .findAllWithMemberAccess(input.query(), actorId)
                .map(file -> FileListOutput.from(file, actorId));
    }

}
