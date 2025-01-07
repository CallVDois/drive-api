package com.callv2.drive.domain.file;

import com.callv2.drive.domain.AggregateRoot;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.handler.Notification;

public class File extends AggregateRoot<FileID> {

    private FileName name;
    private BinaryContent content;

    private File(
            final FileID anId,
            final BinaryContent content) {
        super(anId);
        this.content = content;

        selfValidate();
    }

    @Override
    public void validate(ValidationHandler handler) {
        new FileValidator(this, handler);
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Validation fail has occoured", notification);
    }

    public FileName getName() {
        return name;
    }

    public BinaryContent getContent() {
        return content;
    }

}