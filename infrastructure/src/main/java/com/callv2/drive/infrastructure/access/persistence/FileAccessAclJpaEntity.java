package com.callv2.drive.infrastructure.access.persistence;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "FileAcl")
@Table(name = "file_access_acls")
public class FileAccessAclJpaEntity {

    @EmbeddedId
    private FileAclID id;

    @Enumerated(EnumType.STRING)
    private AccessPermission effectiveAccessPermission;

    public FileAccessAclJpaEntity() {
    }

    private FileAccessAclJpaEntity(
            final FileAclID id,
            final AccessPermission effectiveAccessPermission) {
        this.id = id;
        this.effectiveAccessPermission = effectiveAccessPermission;
    }

    public static FileAccessAclJpaEntity from(
            final Resource<FileID> resource,
            final MemberID grantee,
            final AccessPermission effectiveAccessPermission) {
        return new FileAccessAclJpaEntity(
                FileAclID.from(resource.id().getValue(), grantee.getValue()),
                effectiveAccessPermission);
    }

    public FileAclID getId() {
        return id;
    }

    public void setId(FileAclID id) {
        this.id = id;
    }

    public AccessPermission getEffectiveAccessPermission() {
        return effectiveAccessPermission;
    }

    public void setEffectiveAccessPermission(AccessPermission effectiveAccessPermission) {
        this.effectiveAccessPermission = effectiveAccessPermission;
    }

}
