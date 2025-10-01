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
        return this.fileGateway
                .findAllWithMemberAccess(input.query(), MemberID.of(input.actorId()))
                .map(FileListOutput::from);
    }

}
