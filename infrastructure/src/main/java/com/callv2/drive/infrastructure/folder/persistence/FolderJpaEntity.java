package com.callv2.drive.infrastructure.folder.persistence;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderName;
import com.callv2.drive.domain.folder.FolderSharing;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "Folder")
@Table(name = "folders")
public class FolderJpaEntity {

    @Id
    private UUID id;

    @Column(name = "is_root_folder", nullable = false)
    private Boolean rootFolder;

    @Column(name = "is_default_shared_inbox", nullable = false)
    private Boolean defaultSharedInbox;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "parent_folder_id")
    private UUID parentFolderId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "folder_id", nullable = false)
    private Set<FolderSharingJpaEntity> sharings = new HashSet<>();

    private FolderJpaEntity(
            final UUID id,
            final Boolean rootFolder,
            final Boolean defaultSharedInbox,
            final String name,
            final UUID creatorId,
            final UUID ownerId,
            final UUID parentFolderId,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Set<FolderSharingJpaEntity> sharings) {
        this.id = id;
        this.rootFolder = rootFolder;
        this.defaultSharedInbox = defaultSharedInbox;
        this.name = name;
        this.creatorId = creatorId;
        this.ownerId = ownerId;
        this.parentFolderId = parentFolderId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.sharings = sharings;
    }

    public FolderJpaEntity() {
    }

    public static FolderJpaEntity fromDomain(final Folder folder) {
        final UUID parentFolderId = folder.getParentFolder() == null ? null : folder.getParentFolder().getValue();

        final Set<FolderSharingJpaEntity> sharings = folder
                .getSharings()
                .stream()
                .map(FolderSharingJpaEntity::from)
                .collect(Collectors.toSet());

        final var entity = new FolderJpaEntity(
                folder.getId().getValue(),
                folder.isRootFolder(),
                folder.getDefaultSharedInbox(),
                folder.getName().value(),
                folder.getCreator().getValue(),
                folder.getOwner().getValue(),
                parentFolderId,
                folder.getCreatedAt(),
                folder.getUpdatedAt(),
                folder.getDeletedAt(),
                sharings);

        return entity;
    }

    public Folder toDomain() {

        final Set<FolderSharing> domainSharings = sharings
                .stream()
                .map(FolderSharingJpaEntity::toDomain)
                .collect(Collectors.toSet());

        return Folder.with(
                FolderID.of(id),
                MemberID.of(creatorId),
                MemberID.of(ownerId),
                FolderName.of(name),
                FolderID.of(parentFolderId),
                createdAt,
                updatedAt,
                deletedAt,
                rootFolder,
                defaultSharedInbox,
                domainSharings);
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

    public Boolean getDefaultSharedInbox() {
        return defaultSharedInbox;
    }

    public void setDefaultSharedInbox(Boolean defaultSharedInbox) {
        this.defaultSharedInbox = defaultSharedInbox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
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