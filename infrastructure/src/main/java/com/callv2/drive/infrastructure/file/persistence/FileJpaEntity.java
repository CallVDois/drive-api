package com.callv2.drive.infrastructure.file.persistence;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.file.BinaryContentID;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileExtension;
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

    @Column(name = "extension", nullable = false)
    private String extension;

    @Column(name = "content", nullable = false)
    private UUID content;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    private FileJpaEntity(
            final UUID id,
            final String name,
            final String extension,
            final UUID content,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.extension = extension;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public FileJpaEntity() {
    }

    public static FileJpaEntity fromDomain(final File file) {
        return new FileJpaEntity(
                file.getId().getValue(),
                file.getName().value(),
                file.getExtension().value(),
                file.getContent().getValue(),
                file.getCreatedAt(),
                file.getUpdatedAt());
    }

    public File toDomain() {
        return File.with(
                FileID.of(id),
                FileName.of(name),
                FileExtension.of(extension),
                BinaryContentID.of(content),
                createdAt,
                updatedAt);
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public UUID getContent() {
        return content;
    }

    public void setContent(UUID content) {
        this.content = content;
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
