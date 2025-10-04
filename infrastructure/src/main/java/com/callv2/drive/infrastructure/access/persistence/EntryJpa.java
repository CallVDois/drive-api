package com.callv2.drive.infrastructure.access.persistence;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Entry;
import com.callv2.drive.domain.access.Permission;
import com.callv2.drive.domain.access.SharePermission;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class EntryJpa implements Serializable {

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

    public EntryJpa() {
    }

    private EntryJpa(
            final UUID memberId,
            final Permission.Type permissionType,
            final AccessPermission accessPermission,
            final SharePermission sharePermission,
            final Instant grantedAt) {
        this.memberId = memberId;
        this.permissionType = permissionType;
        this.accessPermission = accessPermission;
        this.sharePermission = sharePermission;
        this.grantedAt = grantedAt;
    }

    public static <P extends Permission<?>> EntryJpa fromDomain(final Entry<P> entry) {

        return switch (entry.permission()) {
            case AccessPermission accessPermission -> new EntryJpa(
                    entry.member().getValue(),
                    Permission.Type.ACCESS,
                    accessPermission,
                    null,
                    entry.grantedAt());
            case SharePermission sharePermission -> new EntryJpa(
                    entry.member().getValue(),
                    Permission.Type.SHARE,
                    null,
                    sharePermission,
                    entry.grantedAt());
            default ->
                throw new IllegalArgumentException("Unsupported permission type: " + entry.permission().getClass());
        };

    }

    public Entry<?> toDomain() {

        return switch (permissionType) {
            case ACCESS -> new Entry<>(
                    MemberID.of(memberId),
                    accessPermission,
                    grantedAt);
            case SHARE -> new Entry<>(
                    MemberID.of(memberId),
                    sharePermission,
                    grantedAt);
        };

    }

}
