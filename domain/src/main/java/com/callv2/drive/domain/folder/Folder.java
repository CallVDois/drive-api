package com.callv2.drive.domain.folder;

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

    private boolean rootFolder;

    private MemberID owner;

    private FolderName name;
    private FolderID parentFolder;
    private Set<SubFolder> subFolders;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Folder(
            final FolderID id,
            final MemberID owner,
            final FolderName name,
            final FolderID parentFolder,
            final Set<SubFolder> subFolders,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final boolean rootFolder) {
        super(id);

        this.owner = owner;
        this.name = name;
        this.parentFolder = parentFolder;
        this.subFolders = subFolders == null ? new HashSet<>() : new HashSet<>(subFolders);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.rootFolder = rootFolder;

        selfValidate();
    }

    public static Folder with(
            final FolderID id,
            final MemberID owner,
            final FolderName name,
            final FolderID parentFolder,
            final Set<SubFolder> subFolders,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final boolean rootFolder) {
        return new Folder(id, owner, name, parentFolder, subFolders, createdAt, updatedAt, deletedAt, rootFolder);
    }

    public static Folder createRoot(final MemberID owner) {
        Instant now = Instant.now();

        return Folder.with(
                FolderID.unique(),
                owner,
                FolderName.of("Root"),
                null,
                new HashSet<>(),
                now,
                now,
                null,
                true);
    }

    public static Folder create(
            final MemberID owner,
            final FolderName name,
            final Folder parentFolder) {

        Instant now = Instant.now();

        final var folder = Folder.with(
                FolderID.unique(),
                owner,
                name,
                parentFolder.getId(),
                new HashSet<>(),
                now,
                now,
                null,
                false);

        parentFolder.addSubFolder(folder);
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

        if (this.subFolders.stream().anyMatch(it -> it.id().equals(parentFolder.getId())))
            notification.append(ValidationError.with("Parent folder cannot be a subfolder"));

        if (this.getId().equals(parentFolder.getId()))
            notification.append(ValidationError.with("Parent folder cannot be the same folder"));

        if (notification.hasError())
            throw ValidationException.with("Error on change parent folder", notification);

        this.parentFolder = parentFolder.getId();
        this.updatedAt = Instant.now();
        return this;
    }

    public Folder addSubFolder(final Folder folder) {

        if (folder == null)
            return this;

        final SubFolder subFolder = SubFolder.from(folder);

        final Notification notification = Notification.create();

        if (this.subFolders.contains(subFolder))
            notification
                    .append(ValidationError.with("SubFolder already exists"));

        if (this.subFolders.stream().anyMatch(it -> it.name().equals(folder.getName())))
            notification
                    .append(ValidationError.with("SubFolder %s already exists".formatted(folder.getName().value())));

        if (notification.hasError())
            throw ValidationException.with("Error on add subfolder", notification);

        this.subFolders.add(subFolder);
        this.updatedAt = Instant.now();

        return this;
    }

    public Folder removeSubFolder(final Folder folder) {

        if (folder == null)
            return this;

        final SubFolder subFolder = SubFolder.from(folder);

        if (!this.subFolders.contains(subFolder))
            return this;

        this.subFolders.remove(subFolder);
        this.updatedAt = Instant.now();

        return this;
    }

    public boolean isRootFolder() {
        return rootFolder;
    }

    public MemberID getOwner() {
        return owner;
    }

    public FolderID getParentFolder() {
        return parentFolder;
    }

    public FolderName getName() {
        return name;
    }

    public Set<SubFolder> getSubFolders() {
        return Set.copyOf(subFolders);
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

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Validation fail has occoured", notification);
    }

}
