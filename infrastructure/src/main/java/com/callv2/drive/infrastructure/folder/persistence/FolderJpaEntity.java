package com.callv2.drive.infrastructure.folder.persistence;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderName;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Folder")
@Table(name = "folders")
public class FolderJpaEntity {

    @Id
    private UUID id;

    @Column(name = "is_root_folder", nullable = false)
    private Boolean rootFolder;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "parent_folder_id")
    private UUID parentFolderId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    private FolderJpaEntity(
            final UUID id,
            final Boolean rootFolder,
            final String name,
            final String ownerId,
            final UUID parentFolderId,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        this.id = id;
        this.rootFolder = rootFolder;
        this.name = name;
        this.ownerId = ownerId;
        this.parentFolderId = parentFolderId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

    }

    public FolderJpaEntity() {
    }

    public static FolderJpaEntity fromDomain(final Folder folder) {
        final UUID parentFolderId = folder.getParentFolder() == null ? null : folder.getParentFolder().getValue();

        final var entity = new FolderJpaEntity(
                folder.getId().getValue(),
                folder.isRootFolder(),
                folder.getName().value(),
                folder.getOwner().getValue(),
                parentFolderId,
                folder.getCreatedAt(),
                folder.getUpdatedAt(),
                folder.getDeletedAt());

        return entity;
    }

    public Folder toDomain() {
        return Folder.with(
                FolderID.of(id),
                MemberID.of(ownerId),
                FolderName.of(name),
                FolderID.of(parentFolderId),
                createdAt,
                updatedAt,
                deletedAt,
                rootFolder);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(Boolean rootFolder) {
        this.rootFolder = rootFolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(UUID parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}