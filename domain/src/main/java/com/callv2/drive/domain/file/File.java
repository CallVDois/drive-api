package com.callv2.drive.domain.file;

import java.time.Instant;

import com.callv2.drive.domain.AggregateRoot;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.handler.Notification;

public class File extends AggregateRoot<FileID> {

    private FileName name;
    private String contentType;
    private String contentLocation;

    private Instant createdAt;
    private Instant updatedAt;

    private File(
            final FileID anId,
            final FileName name,
            final String contentType,
            final String location,
            final Instant createdAt,
            final Instant updatedAt) {
        super(anId);

        this.name = name;
        this.contentType = contentType;
        this.contentLocation = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        selfValidate();
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new FileValidator(this, handler).validate();
    }

    public static File with(
            final FileID id,
            final FileName name,
            final String contentType,
            final String location,
            final Instant createdAt,
            final Instant updatedAt) {
        return new File(id, name, contentType, location, createdAt, updatedAt);
    }

    public static File with(final File file) {
        return File.with(
                file.getId(),
                file.getName(),
                file.getContentType(),
                file.getContentLocation(),
                file.getCreatedAt(),
                file.getUpdatedAt());
    }

    public static File create(
            final FileName name,
            final String contentType,
            final String location) {

        final Instant now = Instant.now();

        return new File(
                FileID.unique(),
                name,
                contentType,
                location,
                now,
                now);
    }

    public File update(
            final FileName name,
            final String contentType) {

        this.name = name;
        this.contentType = contentType;

        this.updatedAt = Instant.now();

        selfValidate();
        return this;
    }

    public FileName getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentLocation() {
        return contentLocation;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Validation fail has occoured", notification);
    }

}