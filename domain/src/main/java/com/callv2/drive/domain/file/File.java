package com.callv2.drive.domain.file;

import java.time.Instant;

import com.callv2.drive.domain.AggregateRoot;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.handler.Notification;

public class File extends AggregateRoot<FileID> {

    private FileName name;
    private FileExtension extension;
    private BinaryContent content;

    private Instant createdAt;
    private Instant updatedAt;

    private File(
            final FileID anId,
            final FileName name,
            final FileExtension extension,
            final BinaryContent content,
            final Instant createdAt,
            final Instant updatedAt) {
        super(anId);

        this.name = name;
        this.extension = extension;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        selfValidate();
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new FileValidator(this, handler).validate();
    }

    public static File create(
            final FileName name,
            final FileExtension extension,
            final BinaryContent content) {

        final Instant now = Instant.now();

        return new File(
                FileID.unique(),
                name,
                extension,
                content,
                now,
                now);
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

    public FileExtension getExtension() {
        return extension;
    }

    public BinaryContent getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}