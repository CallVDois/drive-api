package com.callv2.drive.application.file.create;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.callv2.drive.domain.exception.InternalErrorException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.QuotaExceededException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileContentGateway;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateFileUseCase extends CreateFileUseCase {

    private final MemberGateway memberGateway;
    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;
    private final FileContentGateway contentGateway;

    public DefaultCreateFileUseCase(
            final MemberGateway memberGateway,
            final FolderGateway folderGateway,
            final FileGateway fileGateway,
            final FileContentGateway contentGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.contentGateway = Objects.requireNonNull(contentGateway);
    }

    @Override
    public CreateFileOutput execute(final CreateFileInput input) {

        final MemberID ownerId = MemberID.of(input.ownerId());

        final Member owner = memberGateway
                .findById(ownerId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.ownerId().toString()));

        final Long actualUsedQuota = fileGateway
                .findByOwner(ownerId)
                .stream()
                .map(File::getContent)
                .mapToLong(Content::size)
                .sum();

        if (actualUsedQuota + input.size() > owner.getQuota().sizeInBytes())
            throw QuotaExceededException.with("Quota exceeded",
                    new Error("You have exceeded your actual quota of " + owner.getQuota().sizeInBytes() + " bytes"));

        final FolderID folderId = folderGateway
                .findById(FolderID.of(input.folderId()))
                .map(Folder::getId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        final FileName fileName = FileName.of(input.name());

        final String randomContentName = UUID.randomUUID().toString();
        final String contentLocation = storeContentFile(randomContentName, input.content());
        final String contentType = input.contentType();
        final Long contentSize = input.size();

        final Content content = Content.of(contentLocation, contentType, contentSize);

        final Notification notification = Notification.create();
        final File file = notification.valdiate(() -> File.create(ownerId, folderId, fileName, content));

        List<File> filesOnSameFolder = fileGateway.findByFolder(folderId);
        if (filesOnSameFolder.stream().map(File::getName).anyMatch(fileName::equals))
            notification.append(Error.with("File with same name already exists on this folder"));

        if (notification.hasError())
            throw ValidationException.with("Could not create Aggregate File", notification);

        storeFile(file);

        return CreateFileOutput.from(file);
    }

    private void storeFile(final File file) {
        try {
            fileGateway.create(file);
        } catch (Exception e) {
            deleteContentFile(file.getContent().location());
            throw InternalErrorException.with("Could not store File", e);
        }
    }

    private String storeContentFile(final String contentName, final InputStream inputStream) {
        try {
            return contentGateway.store(contentName, inputStream);
        } catch (Exception e) {
            throw InternalErrorException.with("Could not store BinaryContent", e);
        }
    }

    private void deleteContentFile(final String contentLocation) {
        try {
            contentGateway.delete(contentLocation);
        } catch (Exception e) {
            throw InternalErrorException.with("Could not delete BinaryContent", e);
        }
    }

}
