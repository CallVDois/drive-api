package com.callv2.drive.domain.access;

import java.time.Instant;
import static java.util.Objects.isNull;
import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.member.MemberID;

public record Entry(
        MemberID member,
        AccessPermission accessPermission,
        SharePermission sharePermission,
        Instant grantedAt) implements ValueObject {

    public boolean isEquivalentTo(final Entry other) {

        return isNull(other) ? false
                : member.equals(other.member)
                        && accessPermission.equals(other.accessPermission)
                        && sharePermission.equals(other.sharePermission);

    }

}
