package com.callv2.drive.infrastructure.folder.persistence;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderSharing;
import com.callv2.drive.domain.folder.FolderSharingID;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "FolderSharing")
@Table(name = "folder_sharings")
public class FolderSharingJpaEntity {

    @Id
    private UUID id;

    @Column(name = "shared_to", nullable = false)
    private UUID sharedTo;

    @Column(name = "shared_by", nullable = false)
    private UUID sharedBy;

    @Column(name = "virtual_folder", nullable = false)
    private UUID virtualFolder;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    private FolderJpaEntity folder;

    public FolderSharingJpaEntity() {
    }

    private FolderSharingJpaEntity(
            UUID id,
            UUID sharedTo,
            UUID sharedBy,
            UUID virtualFolder,
            Instant createdAt) {
        this.id = id;
        this.sharedTo = sharedTo;
        this.sharedBy = sharedBy;
        this.virtualFolder = virtualFolder;
        this.createdAt = createdAt;
    }

    public static FolderSharingJpaEntity from(final FolderSharing sharing) {
        return new FolderSharingJpaEntity(
                sharing.getId().getValue(),
                sharing.getSharedTo().getValue(),
                sharing.getSharedBy().getValue(),
                sharing.getVirtualFolder().getValue(),
                sharing.getCreatedAt());
    }

    public FolderSharing toDomain() {
        return FolderSharing.with(
                FolderSharingID.of(this.id),
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

    public FolderJpaEntity getFolder() {
        return folder;
    }

    public void setFolder(FolderJpaEntity folder) {
        this.folder = folder;
    }

}
