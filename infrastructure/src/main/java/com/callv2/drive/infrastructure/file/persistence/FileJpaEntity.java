package com.callv2.drive.infrastructure.file.persistence;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.file.FileName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "File")
@Table(name = "files")
public class FileJpaEntity {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "content_location", nullable = false)
    private String contentLocation;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    private FileJpaEntity(
            final UUID id,
            final String name,
            final String contentType,
            final String contentLocation,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
        this.contentLocation = contentLocation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public FileJpaEntity() {
    }

    public static FileJpaEntity fromDomain(final File file) {

        return new FileJpaEntity(
                file.getId().getValue(),
                file.getName().value(),
                file.getContentType(),
                file.getContentLocation(),
                file.getCreatedAt(),
                file.getUpdatedAt());
    }

    public File toDomain() {
        return File.with(
                FileID.of(getId()),
                FileName.of(getName()),
                getContentType(),
                getContentLocation(),
                getCreatedAt(),
                getUpdatedAt());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
