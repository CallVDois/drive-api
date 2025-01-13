package com.callv2.drive.application.file.create;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

import com.callv2.drive.domain.exception.InternalErrorException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileContentGateway;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateFileUseCase extends CreateFileUseCase {

    private final FileGateway fileGateway;
    private final FileContentGateway contentGateway;

    public DefaultCreateFileUseCase(
            final FileGateway fileGateway,
            final FileContentGateway contentGateway) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.contentGateway = Objects.requireNonNull(contentGateway);
    }

    @Override
    public CreateFileOutput execute(final CreateFileInput input) {

        final FileName fileName = FileName.of(input.name());

        final String randomContentName = UUID.randomUUID().toString();
        final String contentLocation = storeContentFile(randomContentName, input.content());
        final String contentType = input.contentType();
        final Long contentSize = input.size();

        final Content content = Content.of(contentLocation, contentType, contentSize);

        final Notification notification = Notification.create();
        final File file = notification.valdiate(() -> File.create(fileName, content));

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
