package com.callv2.drive.infrastructure.access.persistence;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Entry;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.access.SharePermission;
import com.callv2.drive.domain.file.FileID;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "FileAcl")
@Table(name = "file_acls")
public class FileAclJpaEntity {

    @EmbeddedId
    private FileAclID id;

    @Enumerated(EnumType.STRING)
    private AccessPermission effectiveAccessPermission;

    @Enumerated(EnumType.STRING)
    private SharePermission effectiveSharePermission;

    public FileAclJpaEntity() {
    }

    private FileAclJpaEntity(
            final FileAclID id,
            final AccessPermission effectiveAccessPermission,
            final SharePermission effectiveSharePermission) {
        this.id = id;
        this.effectiveAccessPermission = effectiveAccessPermission;
        this.effectiveSharePermission = effectiveSharePermission;
    }

    public static FileAclJpaEntity from(Resource<FileID> resource, Entry entry) {
        return new FileAclJpaEntity(
                FileAclID.from(resource.id().getValue(), entry.member().getValue()),
                entry.accessPermission(),
                entry.sharePermission());
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

    public SharePermission getEffectiveSharePermission() {
        return effectiveSharePermission;
    }

    public void setEffectiveSharePermission(SharePermission effectiveSharePermission) {
        this.effectiveSharePermission = effectiveSharePermission;
    }

}
