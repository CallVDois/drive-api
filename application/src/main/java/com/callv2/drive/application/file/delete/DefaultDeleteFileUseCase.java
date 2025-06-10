package com.callv2.drive.application.file.delete;

import java.util.Objects;

import com.callv2.drive.domain.exception.InternalErrorException;
import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.storage.StorageService;

public class DefaultDeleteFileUseCase extends DeleteFileUseCase {

    private final MemberGateway memberGateway;
    private final FileGateway fileGateway;
    private final StorageService storageService;

    public DefaultDeleteFileUseCase(
            final MemberGateway memberGateway,
            final FileGateway fileGateway,
            final StorageService storageService) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageService = Objects.requireNonNull(storageService);
    }

    @Override
    public void execute(DeleteFileInput input) {
        final MemberID ownerId = MemberID.of(input.ownerId());
        final FileID fileId = FileID.of(input.fileId());

        final Member member = memberGateway.findById(ownerId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.ownerId()));

        if (!member.hasSystemAccess())
            throw NotAllowedException.with("Member does not have permission to delete files");

        final File file = fileGateway.findById(fileId)
                .orElseThrow(() -> NotFoundException.with(File.class, input.fileId().toString()));

        fileGateway.deleteById(file.getId()); // TODO maybe needs transactional
        deleteContentFile(file.getContent().location());
    }

    private void deleteContentFile(final String contentLocation) {
        try {
            storageService.delete(contentLocation);
        } catch (Exception e) {
            throw InternalErrorException.with("Could not delete BinaryContent", e);
        }
    }

}
