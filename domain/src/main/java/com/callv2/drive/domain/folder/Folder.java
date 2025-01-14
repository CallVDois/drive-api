package com.callv2.drive.domain.folder;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.callv2.drive.domain.AggregateRoot;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.handler.Notification;

public class Folder extends AggregateRoot<FolderID> {

    private FolderName name;
    private FolderID parentFolder;
    private Set<SubFolder> subFolders;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Folder(
            final FolderID id,
            final FolderName name,
            final FolderID parentFolder,
            final Set<SubFolder> subFolders,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(id);
        this.name = name;
        this.parentFolder = parentFolder;
        this.subFolders = new HashSet<>(subFolders);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        selfValidate();
    }

    @Override
    public void validate(ValidationHandler handler) {
        new FolderValidator(this, handler).validate();
    }

    public static Folder create(
            final FolderName name,
            final Folder parentFolder) {

        Instant now = Instant.now();

        return new Folder(
                FolderID.unique(),
                name,
                parentFolder.getId(),
                Set.of(),
                now,
                now,
                null);
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

        if (this.subFolders.stream().anyMatch(it -> it.id().equals(parentFolder.getId())))
            throw ValidationException.with("Error on change parent folder",
                    Error.with("Parent folder cannot be a subfolder"));

        if (this.getId().equals(parentFolder.getId()))
            throw ValidationException.with("Error on change parent folder",
                    Error.with("Parent folder cannot be the same folder"));

        this.parentFolder = parentFolder.getId();
        this.updatedAt = Instant.now();
        return this;
    }

    public Folder addSubFolder(final Folder folder) {

        if (folder == null)
            return this;

        final SubFolder subFolder = SubFolder.from(folder);

        if (this.subFolders.contains(subFolder))
            throw ValidationException.with("Error on add subfolder", Error.with("SubFolder already exists"));

        if (this.subFolders.stream().anyMatch(it -> it.name().equals(folder.getName())))
            throw ValidationException.with("Error on add subfolder",
                    Error.with("SubFolder %s already exists".formatted(folder.getName().value())));

        this.subFolders.add(subFolder);
        this.updatedAt = Instant.now();

        return this;
    }

    public FolderID getParentFolder() {
        return parentFolder;
    }

    public FolderName getName() {
        return name;
    }

    public Set<SubFolder> getSubFolders() {
        return new HashSet<>(subFolders);
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
