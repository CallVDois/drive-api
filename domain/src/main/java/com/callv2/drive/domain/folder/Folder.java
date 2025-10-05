package com.callv2.drive.domain.folder;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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

    private final Set<FolderSharing> sharings;

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
            final Boolean defaultSharedInbox,
            final Set<FolderSharing> sharings) {
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
        this.sharings = nonNull(sharings) ? new HashSet<>(sharings) : new HashSet<>();

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
            final Boolean defaultSharedInbox,
            final Set<FolderSharing> sharings) {
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
                defaultSharedInbox,
                sharings);
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
                Boolean.FALSE,
                new HashSet<>());
    }

    public static Folder createInbox(final MemberID owner, final FolderID parentFolder) {
        Instant now = Instant.now();

        return Folder.with(
                FolderID.unique(),
                owner,
                owner,
                FolderName.of("Shared"),
                parentFolder,
                now,
                now,
                null,
                Boolean.FALSE,
                Boolean.TRUE,
                new HashSet<>());
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
                Boolean.FALSE,
                new HashSet<>());

        return folder;
    }

    public Folder share(final MemberID sharedBy, final MemberID sharedTo, final FolderID virtualFolder) {

        final Notification notification = Notification.create();

        if (isNull(sharedBy) || isNull(sharedTo) || isNull(virtualFolder)) {
            notification.append(ValidationError.with("Member's and virtual folder are required"));
            throw ValidationException.with("Could not share the folder", notification);
        }

        if (this.owner.equals(sharedTo) || sharedBy.equals(sharedTo)) {
            notification.append(ValidationError.with("Cannot share a folder with yourself"));
            throw ValidationException.with("Could not share the folder", notification);
        }

        this.updatedAt = Instant.now();

        final FolderSharing sharing = FolderSharing.create(sharedTo, sharedBy, virtualFolder);

        // this.events.add(FolderSharedEvent.create(this, sharing));

        this.sharings.add(sharing);

        return this;
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

    public FolderID getVirtualParentFolder(final MemberID member) {

        if (this.owner.equals(member))
            return this.parentFolder;

        return this.sharings.stream()
                .filter(sharing -> sharing.getSharedTo().equals(member))
                .findFirst()
                .map(FolderSharing::getVirtualFolder)
                .orElse(null);
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Validation fail has occoured", notification);
    }

    public Boolean isRootFolder() {
        return getRootFolder();
    }

    public Boolean getRootFolder() {
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

    public Set<FolderSharing> getSharings() {
        return Set.copyOf(sharings);
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
