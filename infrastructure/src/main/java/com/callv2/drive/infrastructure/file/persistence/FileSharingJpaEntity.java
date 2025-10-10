package com.callv2.drive.infrastructure.file.persistence;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.file.FileSharing;
import com.callv2.drive.domain.file.FileSharingID;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "FileSharing")
@Table(name = "file_sharings")
public class FileSharingJpaEntity {

    @Id
    private UUID id;

    private UUID sharedTo;

    private UUID sharedBy;

    private UUID virtualFolder;

    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileJpaEntity file;

    public FileSharingJpaEntity() {
    }

    private FileSharingJpaEntity(
            final UUID id,
            final UUID sharedTo,
            final UUID sharedBy,
            final UUID virtualFolder,
            final Instant createdAt,
            final FileJpaEntity file) {
        this.id = id;
        this.sharedTo = sharedTo;
        this.sharedBy = sharedBy;
        this.virtualFolder = virtualFolder;
        this.createdAt = createdAt;
        this.file = file;
    }

    public static FileSharingJpaEntity from(final FileJpaEntity fileJpaEntity, final FileSharing sharing) {
        return new FileSharingJpaEntity(
                sharing.getId().getValue(),
                sharing.getSharedTo().getValue(),
                sharing.getSharedBy().getValue(),
                sharing.getVirtualFolder().getValue(),
                sharing.getCreatedAt(),
                fileJpaEntity);
    }

    public FileSharing toDomain() {
        return FileSharing.with(
                FileSharingID.of(this.id),
                MemberID.of(this.sharedTo),
                MemberID.of(this.sharedBy),
                FolderID.of(this.virtualFolder),
                this.createdAt);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSharedTo() {
        return sharedTo;
    }

    public void setSharedTo(UUID sharedTo) {
        this.sharedTo = sharedTo;
    }

    public UUID getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(UUID sharedBy) {
        this.sharedBy = sharedBy;
    }

    public UUID getVirtualFolder() {
        return virtualFolder;
    }

    public void setVirtualFolder(UUID virtualFolder) {
        this.virtualFolder = virtualFolder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public FileJpaEntity getFile() {
        return file;
    }

    public void setFile(FileJpaEntity file) {
        this.file = file;
    }

}
