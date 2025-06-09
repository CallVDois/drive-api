package com.callv2.drive.application.member.synchronize;

import java.time.Instant;

public record SynchronizeMemberInput(
        String id,
        String username,
        String nickname,
        Instant createdAt,
        Instant updatedAt,
        Long synchronizedVersion) {

    public static SynchronizeMemberInput from(
            final String id,
            final String username,
            final String nickname,
            final Instant createdAt,
            final Instant updatedAt,
            final Long synchronizedVersion) {
        return new SynchronizeMemberInput(id, username, nickname, createdAt, updatedAt, synchronizedVersion);
    }

}
