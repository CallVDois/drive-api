package com.callv2.drive.domain.file;

import java.time.Instant;

import com.callv2.drive.domain.Entity;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationHandler;

public class Sharing extends Entity<SharingID> {

    private MemberID sharedTo;
    private MemberID sharedBy;
    private FolderID virtualFolder;
    private Instant createdAt;

    private Sharing(
            final SharingID id,
            final MemberID sharedTo,
            final MemberID sharedBy,
            final FolderID virtualFolder,
            final Instant createdAt) {
        super(id);
        this.sharedTo = sharedTo;
        this.sharedBy = sharedBy;
        this.virtualFolder = virtualFolder;
        this.createdAt = createdAt;
    }

    public static Sharing with(
            final SharingID id,
            final MemberID sharedTo,
            final MemberID sharedBy,
            final FolderID virtualFolder,
            final Instant createdAt) {
        return new Sharing(
                id,
                sharedTo,
                sharedBy,
                virtualFolder,
                createdAt);
    }

    public static Sharing create(
            final MemberID sharedTo,
            final MemberID sharedBy,
            final FolderID virtualFolder) {
        final Instant now = Instant.now();
        return new Sharing(
                SharingID.unique(),
                sharedTo,
                sharedBy,
                virtualFolder,
                now);
    }

    @Override
    public void validate(final ValidationHandler handler) {
    }

    public MemberID getSharedTo() {
        return sharedTo;
    }

    public MemberID getSharedBy() {
        return sharedBy;
    }

    public FolderID getVirtualFolder() {
        return virtualFolder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
