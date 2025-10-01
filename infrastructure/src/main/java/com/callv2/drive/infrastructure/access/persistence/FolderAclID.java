package com.callv2.drive.infrastructure.access.persistence;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FolderAclID implements Serializable {

    @Column(name = "folder_id", nullable = false)
    private UUID folderId;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    public FolderAclID() {
    }

    private FolderAclID(UUID folderId, UUID memberId) {
        this.folderId = folderId;
        this.memberId = memberId;
    }

    public static FolderAclID from(final UUID folderId, final UUID memberId) {
        return new FolderAclID(folderId, memberId);
    }

    public UUID getFolderId() {
        return folderId;
    }

    public void setFolderId(UUID folderId) {
        this.folderId = folderId;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(folderId, memberId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FolderAclID other = (FolderAclID) obj;
        if (folderId == null) {
            if (other.folderId != null)
                return false;
        } else if (!folderId.equals(other.folderId))
            return false;
        if (memberId == null) {
            if (other.memberId != null)
                return false;
        } else if (!memberId.equals(other.memberId))
            return false;
        return true;
    }

}
