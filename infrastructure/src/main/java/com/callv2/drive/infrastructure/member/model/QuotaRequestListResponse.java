package com.callv2.drive.infrastructure.member.model;

import java.time.Instant;

import com.callv2.drive.domain.member.QuotaUnit;

public record QuotaRequestListResponse(
        Member member,
        long amount,
        QuotaUnit unit,
        Instant requestedAt) {

    public record Member(String id, String username) {
    }

}
