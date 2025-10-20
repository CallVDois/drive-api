package com.callv2.drive.infrastructure.acl.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.callv2.drive.domain.acl.AccessPermission;
import com.callv2.drive.domain.acl.ResourceType;

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
                Instant grantedAt) implements Serializable {

        }

    }
}
