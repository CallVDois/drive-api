package com.callv2.drive.infrastructure.access.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.ResourceType;
import com.callv2.drive.domain.access.SharePermission;

public record UpdateAclMessage(Data data) implements Serializable {

    public record Data(
            UUID aclId,
            String resourceId,
            ResourceType resourceType,
            Set<Data.Entry> directEntries,
            Set<Data.Entry> inheritedEntries,
            Instant creadtedAt,
            Instant updatedAt) implements Serializable {

        public record Entry(
                UUID memberId,
                AccessPermission accessPermission,
                SharePermission sharePermission,
                Instant grantedAt) implements Serializable {

        }

    }
}
