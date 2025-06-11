package com.callv2.drive.infrastructure.messaging.listener.member;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public record MemberSyncEvent(
        String id,
        String source,
        MemberSyncEvent.Data data,
        Instant occurredAt) implements Serializable {

    public record Data(
            String id,
            String username,
            String email,
            String nickname,
            boolean isActive,
            Set<String> systems,
            Instant createdAt,
            Instant updatedAt,
            Long synchronizedVersion) implements Serializable {

    }

}
