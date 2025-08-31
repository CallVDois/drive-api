package com.callv2.drive.domain.file;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import com.callv2.drive.domain.AggregateRoot;
import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventSource;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.handler.Notification;

public class File extends AggregateRoot<FileID> implements EventSource {

    private Queue<Event<?>> events;

    private MemberID owner;

    private FolderID folder;

    private FileName name;
    private Content content;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Boolean isDeleted;

    private File(
            final FileID anId,
            final MemberID owner,
            final FolderID folder,
            final FileName name,
            final Content content,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Boolean isDeleted) {
        super(anId);

        this.folder = folder;
        this.owner = owner;
        this.name = name;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isDeleted = isDeleted;

        this.events = new LinkedList<>();

        selfValidate();
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new FileValidator(this, handler).validate();
    }

    public static File with(
            final FileID id,
            final MemberID owner,
            final FolderID folder,
            final FileName name,
            final Content content,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Boolean isDeleted) {
        return new File(id, owner, folder, name, content, createdAt, updatedAt, deletedAt, isDeleted);
    }

    public static File with(final File file) {
        return File.with(
                file.getId(),
                file.getOwner(),
                file.getFolder(),
                file.getName(),
                file.getContent(),
                file.getCreatedAt(),
                file.getUpdatedAt(),
                file.getDeletedAt(),
                file.getIsDeleted());
    }

    @Override
    public Optional<Event<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    public static File create(
            final MemberID owner,
            final FolderID folder,
            final FileName name,
            final Content content) {

        final Instant now = Instant.now();

        return new File(
                FileID.unique(),
                owner,
                folder,
                name,
                content,
                now,
                now,
                null,
                false);
    }

    public File update(
            final FolderID folder,
            final FileName name,
            final Content content) {

        if (this.folder.equals(folder) && this.name.equals(name) && this.content.equals(content))
            return this;

        this.folder = folder;
        this.name = name;
        this.content = content;

        this.updatedAt = Instant.now();

        selfValidate();
        return this;
    }

    public File delete() {

        if (this.isDeleted)
            return this;

        this.deletedAt = Instant.now();
        this.isDeleted = true;

        this.events.add(FileDeletedEvent.create(this));

        return this;

    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Validation fail has occoured", notification);
    }

    public MemberID getOwner() {
        return owner;
    }

    public FolderID getFolder() {
        return folder;
    }

    public FileName getName() {
        return name;
    }

    public Content getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    @Override
    public String toString() {
        return "File [id=" + id + ", owner=" + owner + ", folder=" + folder + ", name=" + name + ", content=" + content
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", deletedAt=" + deletedAt + ", isDeleted="
                + isDeleted + "]";
    }

}