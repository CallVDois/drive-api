package com.callv2.drive.domain.file;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import com.callv2.drive.domain.AggregateRoot;
import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventSource;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.handler.Notification;

public class File extends AggregateRoot<FileID> implements EventSource {

    private Queue<Event<?>> events;

    private MemberID creator;
    private MemberID owner;

    private FolderID folder;

    private FileName name;
    private Content content;

    private MemberID updatedBy;
    private MemberID deletedBy;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Boolean isDeleted;

    private final Set<FileSharing> sharings;

    private File(
            final FileID anId,
            final MemberID creator,
            final MemberID owner,
            final FolderID folder,
            final FileName name,
            final Content content,
            final MemberID updatedBy,
            final MemberID deletedBy,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Boolean isDeleted,
            final Set<FileSharing> sharings) {
        super(anId);

        this.folder = folder;
        this.creator = creator;
        this.owner = owner;
        this.name = name;
        this.content = content;
        this.updatedBy = updatedBy;
        this.deletedBy = deletedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isDeleted = isDeleted;
        this.sharings = nonNull(sharings) ? new HashSet<>(sharings) : new HashSet<>();

        this.events = new LinkedList<>();

        selfValidate();
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new FileValidator(this, handler).validate();
    }

    public static File with(
            final FileID id,
            final MemberID creator,
            final MemberID owner,
            final FolderID folder,
            final FileName name,
            final Content content,
            final MemberID updatedBy,
            final MemberID deletedBy,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Boolean isDeleted,
            final Set<FileSharing> sharings) {
        return new File(
                id,
                creator,
                owner,
                folder,
                name,
                content,
                updatedBy,
                deletedBy,
                createdAt,
                updatedAt,
                deletedAt,
                isDeleted,
                sharings);
    }

    public static File with(final File file) {
        return File.with(
                file.getId(),
                file.getCreator(),
                file.getOwner(),
                file.getFolder(),
                file.getName(),
                file.getContent(),
                file.getUpdatedBy(),
                file.getDeletedBy(),
                file.getCreatedAt(),
                file.getUpdatedAt(),
                file.getDeletedAt(),
                file.getIsDeleted(),
                file.getSharings());
    }

    @Override
    public Optional<Event<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    public static File create(
            final MemberID creator,
            final MemberID owner,
            final FolderID folder,
            final FileName name,
            final Content content) {

        final Instant now = Instant.now();

        return new File(
                FileID.unique(),
                creator,
                owner,
                folder,
                name,
                content,
                creator,
                null,
                now,
                now,
                null,
                false,
                Set.of());
    }

    public File update(
            final MemberID updater,
            final FolderID folder,
            final FileName name,
            final Content content) {

        if (this.folder.equals(folder) && this.name.equals(name) && this.content.equals(content))
            return this;

        this.folder = folder;
        this.name = name;
        this.content = content;

        this.updatedBy = updater;
        this.updatedAt = Instant.now();

        selfValidate();
        return this;
    }

    public File delete(final MemberID deleterId) {

        if (this.isDeleted)
            return this;

        final Instant now = Instant.now();

        this.updatedBy = deleterId;
        this.updatedAt = now;
        this.deletedBy = deleterId;
        this.deletedAt = now;
        this.isDeleted = true;

        this.events.add(FileDeletedEvent.create(this, deleterId));

        return this;

    }

    public File share(final MemberID sharedBy, final MemberID sharedTo, final FolderID virtualFolder) {

        final Notification notification = Notification.create();

        if (isNull(sharedBy) || isNull(sharedTo) || isNull(virtualFolder)) {
            notification.append(ValidationError.with("Member's and virtual folder are required"));
            throw ValidationException.with("Could not share the file", notification);
        }

        if (this.owner.equals(sharedTo) || sharedBy.equals(sharedTo)) {
            notification.append(ValidationError.with("Cannot share a file with yourself"));
            throw ValidationException.with("Could not share the file", notification);
        }

        if (this.sharings.stream().anyMatch(s -> s.getSharedTo().equals(sharedTo)))
            return this;

        final FileSharing sharing = FileSharing.create(sharedTo, sharedBy, virtualFolder);
        this.sharings.add(sharing);

        this.events.add(FileSharedEvent.create(this, sharing));

        return this;
    }

    public File unshare(final MemberID revokedBy, final FileSharingID sharingId) {

        final Notification notification = Notification.create();

        if (isNull(revokedBy))
            notification.append(ValidationError.with("revokedBy' are required"));

        if (isNull(sharingId))
            notification.append(ValidationError.with("revokedFrom' are required"));

        this.sharings.stream()
                .filter(s -> s.getId().equals(sharingId))
                .findFirst()
                .ifPresent(sharing -> {
                    sharings.remove(sharing);
                    this.events.add(FileUnsharedEvent.create(this, sharing, revokedBy));
                });

        return this;
    }

    public FolderID getVirtualFolder(final MemberID member) {

        if (this.owner.equals(member))
            return this.folder;

        return this.sharings.stream()
                .filter(sharing -> sharing.getSharedTo().equals(member))
                .findFirst()
                .map(FileSharing::getVirtualFolder)
                .orElse(this.folder);
    }

    public Optional<FileSharing> getSharing(final FileSharingID sharingId) {
        return this.sharings.stream()
                .filter(sharing -> sharing.getId().equals(sharingId))
                .findFirst();
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Validation fail has occoured", notification);
    }

    public Queue<Event<?>> getEvents() {
        return events;
    }

    public MemberID getCreator() {
        return creator;
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

    public MemberID getUpdatedBy() {
        return updatedBy;
    }

    public MemberID getDeletedBy() {
        return deletedBy;
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

    public Set<FileSharing> getSharings() {
        return Set.copyOf(sharings);
    }

    @Override
    public String toString() {
        return "File [id=" + id
                + ", creator=" + creator
                + ", owner=" + owner
                + ", folder=" + folder
                + ", name=" + name
                + ", content=" + content
                + ", updatedBy=" + updatedBy
                + ", deletedBy=" + deletedBy
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", deletedAt=" + deletedAt
                + ", isDeleted=" + isDeleted
                + "]";
    }

}