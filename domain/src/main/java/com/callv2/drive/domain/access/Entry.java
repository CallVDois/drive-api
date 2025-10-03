package com.callv2.drive.domain.access;

import static java.util.Objects.isNull;

import java.time.Instant;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.member.MemberID;

public record Entry<P extends Permission<?>>(
        MemberID member,
        P permission,
        Instant grantedAt) implements ValueObject {

    public static <P extends Permission<?>> Entry<P> create(
            final MemberID member,
            final P permission) {
        return new Entry<>(member, permission, Instant.now());
    }

    public Boolean isEquivalentTo(final Entry<? extends Permission<?>> other) {

        return isNull(other) ? false
                : member.equals(other.member())
                        && permission().equals(other.permission());

    }

}
