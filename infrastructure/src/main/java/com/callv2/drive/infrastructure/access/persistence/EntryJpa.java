package com.callv2.drive.infrastructure.access.persistence;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Entry;
import com.callv2.drive.domain.access.SharePermission;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class EntryJpa implements Serializable {

    @Column(name = "member_id")
    private UUID memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_permission")
    private AccessPermission accessPermission;

    @Enumerated(EnumType.STRING)
    @Column(name = "share_permission")
    private SharePermission sharePermission;

    @Column(name = "granted_at")
    private Instant grantedAt;

    public EntryJpa() {
    }

    private EntryJpa(
            final UUID memberId,
            final AccessPermission accessPermission,
            final SharePermission sharePermission,
            final Instant grantedAt) {
        this.memberId = memberId;
        this.accessPermission = accessPermission;
        this.sharePermission = sharePermission;
        this.grantedAt = grantedAt;
    }

    public static EntryJpa fromDomain(final Entry entry) {
        return new EntryJpa(
                entry.member().getValue(),
                entry.accessPermission(),
                entry.sharePermission(),
                entry.grantedAt());
    }

    public Entry toDomain() {
        return new Entry(
                MemberID.of(memberId),
                accessPermission,
                sharePermission,
                grantedAt);
    }

}
