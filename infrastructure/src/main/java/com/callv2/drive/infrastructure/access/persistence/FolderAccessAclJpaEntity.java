package com.callv2.drive.infrastructure.access.persistence;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "FolderAccessAcl")
@Table(name = "folder_access_acls")
public class FolderAccessAclJpaEntity {

    @EmbeddedId
    private FolderAclID id;

    @Enumerated(EnumType.STRING)
    private AccessPermission effectiveAccessPermission;

    public FolderAccessAclJpaEntity() {
    }

    private FolderAccessAclJpaEntity(
            final FolderAclID id,
            final AccessPermission effectiveAccessPermission) {
        this.id = id;
        this.effectiveAccessPermission = effectiveAccessPermission;
    }

    public static FolderAccessAclJpaEntity from(
            final Resource<FolderID> resource,
            final MemberID grantee,
            final AccessPermission effectiveAccessPermission) {
        return new FolderAccessAclJpaEntity(
                FolderAclID.from(resource.id().getValue(), grantee.getValue()),
                effectiveAccessPermission);
    }

    public FolderAclID getId() {
        return id;
    }

    public void setId(FolderAclID id) {
        this.id = id;
    }

    public AccessPermission getEffectiveAccessPermission() {
        return effectiveAccessPermission;
    }

    public void setEffectiveAccessPermission(AccessPermission effectiveAccessPermission) {
        this.effectiveAccessPermission = effectiveAccessPermission;
    }

}
