package com.callv2.drive.infrastructure.folder.persistence;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderName;
import com.callv2.drive.domain.folder.SubFolder;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "parent_folder_id")
    private UUID parentFolderId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SubFolderJpaEntity> subFolders;

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

        this.subFolders = new HashSet<>();
    }

    public FolderJpaEntity() {
    }

    public static FolderJpaEntity fromDomain(final Folder folder) {
        final UUID parentFolderId = folder.getParentFolder() == null ? null : folder.getParentFolder().getValue();
        final String ownerId = folder.getOwner() == null ? null : folder.getOwner().getValue();

        final var entity = new FolderJpaEntity(
                folder.getId().getValue(),
                folder.isRootFolder(),
                folder.getName().value(),
                ownerId,
                parentFolderId,
                folder.getCreatedAt(),
                folder.getUpdatedAt(),
                folder.getDeletedAt());

        folder.getSubFolders()
                .forEach(entity::addSubFolder);

        return entity;
    }

    public Folder toDomain() {
        return Folder.with(
                FolderID.of(id),
                ownerId != null ? MemberID.of(ownerId) : null,
                FolderName.of(name),
                FolderID.of(parentFolderId),
                subFolders.stream()
                        .map(SubFolderJpaEntity::toDomain)
                        .collect(Collectors.toSet()),
                createdAt,
                updatedAt,
                deletedAt,
                rootFolder);
    }

    public void addSubFolder(final SubFolder anId) {
        this.subFolders.add(SubFolderJpaEntity.with(this, anId));
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

    public Set<SubFolderJpaEntity> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(Set<SubFolderJpaEntity> subFolders) {
        this.subFolders = subFolders;
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