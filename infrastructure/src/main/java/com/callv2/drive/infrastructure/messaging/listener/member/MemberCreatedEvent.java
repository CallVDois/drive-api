package com.callv2.drive.infrastructure.messaging.listener.member;

import java.io.Serializable;
import java.time.Instant;

public record MemberCreatedEvent(
        String id,
        String source,
        MemberCreatedEvent.Data data,
        Instant occurredAt) implements Serializable {

    public record Data(
            String id,
            String username,
            String email,
            String nickname,
            boolean isActive,
            Instant createdAt,
            Instant updatedAt) implements Serializable {

    }

}
