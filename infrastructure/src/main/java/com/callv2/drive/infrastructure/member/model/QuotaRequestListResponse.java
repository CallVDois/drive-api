package com.callv2.drive.infrastructure.member.model;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.member.QuotaUnit;

public record QuotaRequestListResponse(
        Member member,
        long amount,
        QuotaUnit unit,
        Instant requestedAt) {

    public record Member(UUID id, String username) {
    }

}
