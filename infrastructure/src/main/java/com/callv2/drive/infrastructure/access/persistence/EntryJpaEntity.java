package com.callv2.drive.infrastructure.access.persistence;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Entry;
import com.callv2.drive.domain.access.EntryID;
import com.callv2.drive.domain.access.Permission;
import com.callv2.drive.domain.access.SharePermission;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "share_permission")
    private SharePermission sharePermission;

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
            final SharePermission sharePermission,
            final Instant grantedAt) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.permissionType = permissionType;
        this.accessPermission = accessPermission;
        this.sharePermission = sharePermission;
        this.grantedAt = grantedAt;
    }

    public static <P extends Permission<?>> EntryJpaEntity fromDomain(
            final Entry<P> entry,
            final EntryJpaEntity.Type type) {

        return switch (entry.getPermission()) {
            case AccessPermission accessPermission -> new EntryJpaEntity(
                    entry.getId().getValue(),
                    entry.getMember().getValue(),
                    type,
                    Permission.Type.ACCESS,
                    accessPermission,
                    null,
                    entry.getGrantedAt());
            case SharePermission sharePermission -> new EntryJpaEntity(
                    entry.getId().getValue(),
                    entry.getMember().getValue(),
                    type,
                    Permission.Type.SHARE,
                    null,
                    sharePermission,
                    entry.getGrantedAt());
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
            case SHARE -> Entry.with(
                    EntryID.of(this.id),
                    MemberID.of(memberId),
                    sharePermission,
                    grantedAt);
        };

    }

    public enum Type {
        DIRECT,
        INHERITED
    }

}
