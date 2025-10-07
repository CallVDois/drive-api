package com.callv2.drive.infrastructure.access.persistence;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Entry;
import com.callv2.drive.domain.access.EntryID;
import com.callv2.drive.domain.access.Permission;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "EntryJpa")
@Table(name = "acl_entries")
public class EntryJpaEntity implements Serializable {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false)
    private EntryJpaEntity.Type type;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_type", nullable = false)
    private Permission.Type permissionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_permission")
    private AccessPermission accessPermission;

    @Column(name = "granted_at", nullable = false)
    private Instant grantedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acl_id", nullable = false)
    private AclJpaEntity acl;

    public EntryJpaEntity() {
    }

    private EntryJpaEntity(
            final UUID id,
            final UUID memberId,
            final EntryJpaEntity.Type type,
            final Permission.Type permissionType,
            final AccessPermission accessPermission,
            final Instant grantedAt,
            final AclJpaEntity acl) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.permissionType = permissionType;
        this.accessPermission = accessPermission;
        this.grantedAt = grantedAt;
        this.acl = acl;
    }

    public static <P extends Permission<?>> EntryJpaEntity fromDomain(
            final AclJpaEntity aclJpa,
            final Entry<P> entry,
            final EntryJpaEntity.Type type) {

        return switch (entry.getPermission()) {
            case AccessPermission accessPermission -> new EntryJpaEntity(
                    entry.getId().getValue(),
                    entry.getMember().getValue(),
                    type,
                    Permission.Type.ACCESS,
                    accessPermission,
                    entry.getGrantedAt(),
                    aclJpa);
            default ->
                throw new IllegalArgumentException("Unsupported permission type: " + entry.getPermission().getClass());
        };

    }

    public Entry<?> toDomain() {

        return switch (permissionType) {
            case ACCESS -> Entry.with(
                    EntryID.of(this.id),
                    MemberID.of(memberId),
                    accessPermission,
                    grantedAt);
        };

    }

    public enum Type {
        DIRECT,
        INHERITED
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EntryJpaEntity.Type getType() {
        return type;
    }

    public void setType(EntryJpaEntity.Type type) {
        this.type = type;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public Permission.Type getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Permission.Type permissionType) {
        this.permissionType = permissionType;
    }

    public AccessPermission getAccessPermission() {
        return accessPermission;
    }

    public void setAccessPermission(AccessPermission accessPermission) {
        this.accessPermission = accessPermission;
    }

    public Instant getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(Instant grantedAt) {
        this.grantedAt = grantedAt;
    }

    public AclJpaEntity getAcl() {
        return acl;
    }

    public void setAcl(AclJpaEntity acl) {
        this.acl = acl;
    }

}
