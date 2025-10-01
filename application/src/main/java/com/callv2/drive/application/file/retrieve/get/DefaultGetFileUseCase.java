package com.callv2.drive.application.file.retrieve.get;

import java.util.Objects;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.member.MemberID;

public class DefaultGetFileUseCase extends GetFileUseCase {

    private final FileGateway fileGateway;

    public DefaultGetFileUseCase(final FileGateway fileGateway) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetFileOutput execute(GetFileInput input) {
        return fileGateway
                .findByIdWithMemberAccess(FileID.of(input.fileId()), MemberID.of(input.actorId()))
                .map(GetFileOutput::from)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));
    }

}
