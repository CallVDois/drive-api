package com.callv2.drive.application.file.usecase.content.delete;

import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.storage.StorageGateway;

public class DefaultlDeleteFileContentUseCase extends DeleteFileContentUseCase {

    private final FileGateway fileGateway;
    private final StorageGateway storageService;

    public DefaultlDeleteFileContentUseCase(
            FileGateway fileGateway,
            StorageGateway storageService) {
        this.fileGateway = fileGateway;
        this.storageService = storageService;
    }

    @Override
    public void execute(final DeleteFileContentInput input) {

        fileGateway
                .findByIdWithMemberAccess(FileID.of(input.fileId()), MemberID.of(input.deleterId()))
                .ifPresent(this::fullFileDeletion);

    }

    private void fullFileDeletion(final File file) {
        fileGateway.deleteById(file.getId());
        storageService.delete(file.getContent().storageKey());
    }

}