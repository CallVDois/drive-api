package com.callv2.drive.infrastructure.folder.persistence;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class SubFolderID implements Serializable {

    @Column(name = "parent_folder_id", nullable = false)
    private UUID parentFolderId;

    @Column(name = "sub_folder_id", nullable = false)
    private UUID subFolderId;

    public SubFolderID() {
    }

    private SubFolderID(UUID parentFolderId, UUID subFolderId) {
        this.parentFolderId = parentFolderId;
        this.subFolderId = subFolderId;
    }

    public static SubFolderID with(UUID parentFolderId, UUID subFolderId) {
        return new SubFolderID(parentFolderId, subFolderId);
    }

    public UUID getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(UUID parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public UUID getSubFolderId() {
        return subFolderId;
    }

    public void setSubFolderId(UUID subFolderId) {
        this.subFolderId = subFolderId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parentFolderId == null) ? 0 : parentFolderId.hashCode());
        result = prime * result + ((subFolderId == null) ? 0 : subFolderId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubFolderID other = (SubFolderID) obj;
        if (parentFolderId == null) {
            if (other.parentFolderId != null)
                return false;
        } else if (!parentFolderId.equals(other.parentFolderId))
            return false;
        if (subFolderId == null) {
            if (other.subFolderId != null)
                return false;
        } else if (!subFolderId.equals(other.subFolderId))
            return false;
        return true;
    }

}
