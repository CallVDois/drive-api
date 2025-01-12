package com.callv2.drive.application.file.content.get;

import java.util.Objects;
import java.util.UUID;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileContentGateway;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;

public class DefaultGetFileContentUseCase extends GetFileContentUseCase {

    private final FileContentGateway contentGateway;
    private final FileGateway fileGateway;

    public DefaultGetFileContentUseCase(
            final FileContentGateway contentGateway,
            final FileGateway fileGateway) {
        this.contentGateway = Objects.requireNonNull(contentGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetFileContentOutput execute(GetFileContentInput input) {

        final FileID fileId = FileID.of(UUID.fromString(input.fileId()));

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId()));

        final var content = contentGateway.load(file);

        return GetFileContentOutput.with(file.getName().value(), content.inputStream(), content.size());
    }

}
