package com.callv2.drive.infrastructure.acl.persistence;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class FileAclID implements Serializable {

    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    public FileAclID() {
    }

    private FileAclID(UUID fileId, UUID memberId) {
        this.fileId = fileId;
        this.memberId = memberId;
    }

    public static FileAclID from(final UUID fileId, final UUID memberId) {
        return new FileAclID(fileId, memberId);
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, memberId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileAclID other = (FileAclID) obj;
        if (fileId == null) {
            if (other.fileId != null)
                return false;
        } else if (!fileId.equals(other.fileId))
            return false;
        if (memberId == null) {
            if (other.memberId != null)
                return false;
        } else if (!memberId.equals(other.memberId))
            return false;
        return true;
    }

}
