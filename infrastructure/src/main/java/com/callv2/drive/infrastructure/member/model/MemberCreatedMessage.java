package com.callv2.drive.infrastructure.member.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public record MemberCreatedMessage(Data data) implements Serializable {

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
