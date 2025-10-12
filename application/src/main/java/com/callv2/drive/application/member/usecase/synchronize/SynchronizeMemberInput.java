package com.callv2.drive.application.member.usecase.synchronize;

import java.time.Instant;
import java.util.UUID;

public record SynchronizeMemberInput(
        UUID id,
        String username,
        String nickname,
        Boolean hasSystemAccess,
        Instant createdAt,
        Instant updatedAt,
        Long synchronizedVersion) {

    public static SynchronizeMemberInput from(
            final UUID id,
            final String username,
            final String nickname,
            final Boolean hasSystemAccess,
            final Instant createdAt,
            final Instant updatedAt,
            final Long synchronizedVersion) {
        return new SynchronizeMemberInput(id, username, nickname, hasSystemAccess, createdAt, updatedAt, synchronizedVersion);
    }

}
