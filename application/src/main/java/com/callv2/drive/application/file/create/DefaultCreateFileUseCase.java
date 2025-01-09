package com.callv2.drive.application.file.create;

import java.util.Objects;

import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.file.BinaryContent;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileExtension;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateFileUseCase extends CreateFileUseCase {

    private final FileGateway fileGateway;

    public DefaultCreateFileUseCase(final FileGateway fileGateway) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public CreateFileOutput execute(final CreateFileInput input) {

        final FileName fileName = FileName.of(input.name());
        final FileExtension fileExtension = FileExtension.of(input.extension());
        final BinaryContent binaryContent = BinaryContent.of(input.content());

        final Notification notification = Notification.create();
        final File file = notification.valdiate(() -> File.create(fileName, fileExtension, binaryContent));

        if (notification.hasError())
            throw ValidationException.with("Could not create Aggregate File", notification);

        return CreateFileOutput.from(fileGateway.create(file));
    }

}
