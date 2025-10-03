package com.callv2.drive.infrastructure.access.persistence;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.access.SharePermission;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "FolderAcl")
@Table(name = "folder_acls")
public class FolderAclJpaEntity {

    @EmbeddedId
    private FolderAclID id;

    @Enumerated(EnumType.STRING)
    private AccessPermission effectiveAccessPermission;

    @Enumerated(EnumType.STRING)
    private SharePermission effectiveSharePermission;

    public FolderAclJpaEntity() {
    }

    private FolderAclJpaEntity(
            final FolderAclID id,
            final AccessPermission effectiveAccessPermission,
            final SharePermission effectiveSharePermission) {
        this.id = id;
        this.effectiveAccessPermission = effectiveAccessPermission;
        this.effectiveSharePermission = effectiveSharePermission;
    }

    public static FolderAclJpaEntity from(
            final Resource<FolderID> resource,
            final MemberID grantee,
            final AccessPermission effectiveAccessPermission,
            final SharePermission effectiveSharePermission) {
        return new FolderAclJpaEntity(
                FolderAclID.from(resource.id().getValue(), grantee.getValue()),
                effectiveAccessPermission,
                effectiveSharePermission);
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

    public SharePermission getEffectiveSharePermission() {
        return effectiveSharePermission;
    }

    public void setEffectiveSharePermission(SharePermission effectiveSharePermission) {
        this.effectiveSharePermission = effectiveSharePermission;
    }

}
