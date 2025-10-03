package com.callv2.drive.domain.access;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventEntity;

public class AclUpdatedEvent extends Event<AclUpdatedEvent.Data> {

    private static final String ENTITY = "acl";
    private static final String ACTION = "updated";
    private static final String VERSION = "1.0.0";

    private AclUpdatedEvent() {
        super(ENTITY, ACTION, VERSION, null, null, null);
    }

    private AclUpdatedEvent(
            final Instant occurredAt,
            final Set<EventEntity> relatedEntities,
            final AclUpdatedEvent.Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    public record Data(
            UUID aclId,
            String resourceId,
            ResourceType resourceType,
            Set<Data.Entry> directEntries,
            Set<Data.Entry> inheritedEntries,
            Instant creadtedAt,
            Instant updatedAt) implements Serializable {

        public static Data of(final Acl acl) {
            return new Data(
                    acl.getId().getValue(),
                    acl.getResource().id().getStringValue(),
                    acl.getResource().type(),
                    Entry.of(acl.getDirectEntries()),
                    Entry.of(acl.getInheritedEntries()),
                    acl.getCreatedAt(),
                    acl.getUpdatedAt());
        }

        public static record Entry(
                UUID memberId,
                Permission<?> permission,
                Instant grantedAt) implements Serializable {

            public static Set<Data.Entry> of(final Set<com.callv2.drive.domain.access.Entry<?>> entries) {
                return entries
                        .stream().map(
                                entry -> new Data.Entry(
                                        entry.member().getValue(),
                                        entry.permission(),
                                        entry.grantedAt()))
                        .collect(Collectors.toSet());
            }

        }

    }

    public static AclUpdatedEvent create(final Acl acl) {
        return new AclUpdatedEvent(
                Instant.now(),
                Set.of(EventEntity.of(acl)),
                Data.of(acl));
    }

    public static String eventKey() {
        return new AclUpdatedEvent().key();
    }

}
