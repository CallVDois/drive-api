package com.callv2.drive.application.file.content.get;

import java.util.Objects;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.storage.StorageService;

public class DefaultGetFileContentUseCase extends GetFileContentUseCase {

    private final FileGateway fileGateway;
    private final StorageService storageService;

    public DefaultGetFileContentUseCase(
            final FileGateway fileGateway,
            final StorageService storageService) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageService = Objects.requireNonNull(storageService);
    }

    @Override
    public GetFileContentOutput execute(GetFileContentInput input) {

        final FileID fileId = FileID.of(input.fileId());

        final File file = fileGateway
                .findById(fileId)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        return GetFileContentOutput.with(
                file.getName().value(),
                file.getContent().size(),
                storageService.retrieve(file.getContent().storageKey()));

    }

}
