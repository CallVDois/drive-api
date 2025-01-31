package com.callv2.drive.infrastructure.member.model;

import java.time.Instant;

import com.callv2.drive.domain.member.QuotaUnit;

public record QuotaRequestListResponse(
        String memberId,
        long amount,
        QuotaUnit unit,
        Instant requestedAt) {

}
