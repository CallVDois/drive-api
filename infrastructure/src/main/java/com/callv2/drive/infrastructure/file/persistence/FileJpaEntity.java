package com.callv2.drive.infrastructure.file.persistence;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.folder.FolderID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "File")
@Table(name = "files")
public class FileJpaEntity {

    @Id
    private UUID id;

    @Column(name = "folder_id", nullable = false)
    private UUID folderId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "content_location", nullable = false)
    private String contentLocation;

    @Column(name = "content_size", nullable = false)
    private Long contentSize;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    private FileJpaEntity(
            final UUID id,
            final UUID folderId,
            final String name,
            final String contentType,
            final String contentLocation,
            final Long contentSize,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.folderId = folderId;
        this.name = name;
        this.contentType = contentType;
        this.contentLocation = contentLocation;
        this.contentSize = contentSize;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public FileJpaEntity() {
    }

    public static FileJpaEntity from(final File file) {
        return new FileJpaEntity(
                file.getId().getValue(),
                file.getFolder().getValue(),
                file.getName().value(),
                file.getContent().type(),
                file.getContent().location(),
                file.getContent().size(),
                file.getCreatedAt(),
                file.getUpdatedAt());
    }

    public File toDomain() {
        return File.with(
                FileID.of(getId()),
                FolderID.of(getFolderId()),
                FileName.of(getName()),
                Content.of(getContentLocation(), getContentType(), getContentSize()),
                getCreatedAt(),
                getUpdatedAt());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getContentLocation() {
        return contentLocation;
    }

    public void setContentLocation(String contentLocation) {
        this.contentLocation = contentLocation;
    }

    public Long getContentSize() {
        return contentSize;
    }

    public void setContentSize(Long contentSize) {
        this.contentSize = contentSize;
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

}
