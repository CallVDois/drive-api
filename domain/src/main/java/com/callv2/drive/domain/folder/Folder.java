package com.callv2.drive.domain.folder;

import java.time.Instant;

import com.callv2.drive.domain.AggregateRoot;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.handler.Notification;

public class Folder extends AggregateRoot<FolderID> {

    private Boolean rootFolder;
    private Boolean defaultSharedInbox;

    private MemberID creator;
    private MemberID owner;

    private FolderName name;
    private FolderID parentFolder;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Folder(
            final FolderID id,
            final MemberID creator,
            final MemberID owner,
            final FolderName name,
            final FolderID parentFolder,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Boolean rootFolder,
            final Boolean defaultSharedInbox) {
        super(id);

        this.owner = owner;
        this.creator = creator;
        this.name = name;
        this.parentFolder = parentFolder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.rootFolder = rootFolder;
        this.defaultSharedInbox = defaultSharedInbox;

        selfValidate();
    }

    public static Folder with(
            final FolderID id,
            final MemberID creator,
            final MemberID owner,
            final FolderName name,
            final FolderID parentFolder,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Boolean rootFolder,
            final Boolean defaultSharedInbox) {
        return new Folder(
                id,
                creator,
                owner,
                name,
                parentFolder,
                createdAt,
                updatedAt,
                deletedAt,
                rootFolder,
                defaultSharedInbox);
    }

    public static Folder createRoot(final MemberID creator) {
        Instant now = Instant.now();

        return Folder.with(
                FolderID.unique(),
                creator,
                creator,
                FolderName.of("Root"),
                null,
                now,
                now,
                null,
                Boolean.TRUE,
                Boolean.FALSE);
    }

    public static Folder createInbox(final MemberID owner, final FolderID parentFolder) {
        Instant now = Instant.now();

        return Folder.with(
                FolderID.unique(),
                owner,
                owner,
                FolderName.of("Shared Folder"),
                parentFolder,
                now,
                now,
                null,
                Boolean.FALSE,
                Boolean.TRUE);
    }

    public static Folder create(
            final MemberID creator,
            final MemberID owner,
            final FolderName name,
            final Folder parentFolder) {

        Instant now = Instant.now();

        final var folder = Folder.with(
                FolderID.unique(),
                creator,
                owner,
                name,
                parentFolder.getId(),
                now,
                now,
                null,
                Boolean.FALSE,
                Boolean.FALSE);

        return folder;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new FolderValidator(this, handler).validate();
    }

    public Folder changeName(final FolderName name) {
        this.name = name;
        this.selfValidate();
        this.updatedAt = Instant.now();
        return this;
    }

    public Folder changeParentFolder(final Folder parentFolder) {

        if (parentFolder == null)
            return this;

        if (this.parentFolder.equals(parentFolder.getId()))
            return this;

        final Notification notification = Notification.create();

        if (this.getId().equals(parentFolder.getId()))
            notification.append(ValidationError.with("Parent folder cannot be the same folder"));

        if (notification.hasError())
            throw ValidationException.with("Error on change parent folder", notification);

        this.parentFolder = parentFolder.getId();
        this.updatedAt = Instant.now();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Validation fail has occoured", notification);
    }

    public boolean isRootFolder() {
        return rootFolder;
    }

    public Boolean getDefaultSharedInbox() {
        return defaultSharedInbox;
    }

    public MemberID getCreator() {
        return creator;
    }

    public MemberID getOwner() {
        return owner;
    }

    public FolderName getName() {
        return name;
    }

    public FolderID getParentFolder() {
        return parentFolder;
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

    @Override
    public String toString() {
        return "Folder [id=" + id
                + ", rootFolder=" + rootFolder
                + ", creator=" + creator
                + ", owner=" + owner
                + ", name=" + name
                + ", parentFolder=" + parentFolder
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", deletedAt=" + deletedAt
                + "]";
    }

}
