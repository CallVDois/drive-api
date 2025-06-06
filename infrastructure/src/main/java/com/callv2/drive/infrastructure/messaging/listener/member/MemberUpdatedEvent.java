package com.callv2.drive.infrastructure.messaging.listener.member;

import java.io.Serializable;
import java.time.Instant;

public record MemberUpdatedEvent(
        String id,
        String source,
        MemberUpdatedEvent.Data data,
        Instant occurredAt) implements Serializable {

    public record Data(
            String id,
            String username,
            String email,
            String nickname,
            boolean isActive,
            Instant createdAt,
            Instant updatedAt,
            Long version) implements Serializable {

    }

}
