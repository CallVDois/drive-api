package com.callv2.drive.application.file.content.get;

import java.util.Objects;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;

public class DefaultGetFileContentUseCase extends GetFileContentUseCase {

    private final FileGateway fileGateway;

    public DefaultGetFileContentUseCase(final FileGateway fileGateway) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetFileContentOutput execute(GetFileContentInput input) {

        final FileID fileId = FileID.of(input.fileId());

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        return GetFileContentOutput.with(
                file.getName().value(),
                file.getContent().location(),
                file.getContent().size());
    }

}
