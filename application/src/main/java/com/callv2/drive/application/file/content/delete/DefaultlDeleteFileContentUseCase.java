package com.callv2.drive.application.file.content.delete;

import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.storage.StorageService;

public class DefaultlDeleteFileContentUseCase extends DeleteFileContentUseCase {

    private final FileGateway fileGateway;
    private final StorageService storageService;

    public DefaultlDeleteFileContentUseCase(
            FileGateway fileGateway,
            StorageService storageService) {
        this.fileGateway = fileGateway;
        this.storageService = storageService;
    }

    @Override
    public void execute(final DeleteFileContentInput input) {

        fileGateway
                .findById(FileID.of(input.id()))
                .map(File::getContent)
                .map(Content::storageKey)
                .ifPresent(storageService::delete);

    }

}