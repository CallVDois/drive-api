package com.callv2.drive.infrastructure.file.persistence;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.file.FileSharing;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "File")
@Table(name = "files")
public class FileJpaEntity {

    @Id
    private UUID id;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "folder_id", nullable = false)
    private UUID folderId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "content_storage_key", nullable = false)
    private String contentStorageKey;

    @Column(name = "content_size", nullable = false)
    private Long contentSize;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @Column(name = "updated_by", nullable = false)
    private UUID updatedBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    private FileJpaEntity(
            final UUID id,
            final UUID creatorId,
            final UUID ownerId,
            final UUID folderId,
            final String name,
            final String contentType,
            final String contentLocation,
            final Long contentSize,
            final UUID updatedBy,
            final UUID deletedBy,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Boolean isDeleted) {
        this.id = id;
        this.creatorId = creatorId;
        this.ownerId = ownerId;
        this.folderId = folderId;
        this.name = name;
        this.contentType = contentType;
        this.contentStorageKey = contentLocation;
        this.contentSize = contentSize;
        this.updatedBy = updatedBy;
        this.deletedBy = deletedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isDeleted = isDeleted;
    }

    public FileJpaEntity() {
    }

    public static FileJpaEntity fromDomain(final File file) {

        return new FileJpaEntity(
                file.getId().getValue(),
                file.getCreator().getValue(),
                file.getOwner().getValue(),
                file.getFolder().getValue(),
                file.getName().value(),
                file.getContent().type(),
                file.getContent().storageKey(),
                file.getContent().size(),
                file.getUpdatedBy().getValue(),
                file.getDeletedBy() != null ? file.getDeletedBy().getValue() : null,
                file.getCreatedAt(),
                file.getUpdatedAt(),
                file.getDeletedAt(),
                file.getIsDeleted());
    }

    public File toDomain(final Set<FileSharing> domainSharings) {

        return File.with(
                FileID.of(getId()),
                MemberID.of(getCreatorId()),
                MemberID.of(getOwnerId()),
                FolderID.of(getFolderId()),
                FileName.of(getName()),
                Content.of(getContentStorageKey(), getContentType(), getContentSize()),
                MemberID.of(getUpdatedBy()),
                getDeletedBy() != null ? MemberID.of(getDeletedBy()) : null,
                getCreatedAt(),
                getUpdatedAt(),
                getDeletedAt(),
                getIsDeleted(),
                domainSharings);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public UUID getFolderId() {
        return folderId;
    }

    public void setFolderId(UUID folderId) {
        this.folderId = folderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentStorageKey() {
        return contentStorageKey;
    }

    public void setContentStorageKey(String contentStorageKey) {
        this.contentStorageKey = contentStorageKey;
    }

    public Long getContentSize() {
        return contentSize;
    }

    public void setContentSize(Long contentSize) {
        this.contentSize = contentSize;
    }

    public UUID getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(UUID deletedBy) {
        this.deletedBy = deletedBy;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
